package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState
import org.bson.types.ObjectId
import org.junit.BeforeClass
import org.junit.Test

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType

import static org.junit.Assert.*

/**
 * Date: 11/15/2014
 * Time: 3:29 PM
 */
class TwistedHangmanIntegration extends AbstractGameIntegration<Game, MaskedGame> {
    public static final String LOREMIPSUM = 'Lorem ipsum'

    @Override
    Class<MaskedGame> returnedGameClass() {
        return MaskedGame.class
    }

    Class<Game> internalGameClass() {
        return Game.class
    }

    Game newGame() {
        return new Game()
    }

    AbstractGameRepository gameRepository() {
        return gameRepository
    }

    static GameRepository gameRepository

    @BeforeClass
    static void createPuzzle() {
        PreMadePuzzle puzzle = new PreMadePuzzle()
        puzzle.category = 'PHRASE'
        puzzle.wordPhrase = LOREMIPSUM
        puzzle.source = 'test'
        applicationContext.getBean(PreMadePuzzleRepository.class).save(puzzle)
        gameRepository = applicationContext.getBean(GameRepository.class)
    }

    @Test
    void testGetFeatures() {
        def client = createAPITarget(TEST_PLAYER2)
        def features = client
                .path("features")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<Map<String, String>>() {
        })
        assertEquals([
                "Thieving"               : GameFeature.Thieving.description,
                "Live"                   : GameFeature.Live.description,
                "TurnBased"              : GameFeature.TurnBased.description,
                "Head2Head"              : GameFeature.Head2Head.description,
                "SystemPuzzles"          : GameFeature.SystemPuzzles.description,
                "AlternatingPuzzleSetter": GameFeature.AlternatingPuzzleSetter.description,
                "AllComplete"            : GameFeature.AllComplete.description,
                "SingleWinner"           : GameFeature.SingleWinner.description,
                "DrawGallows"            : GameFeature.DrawGallows.description,
                "DrawFace"               : GameFeature.DrawFace.description
        ], features)
    }

    @Test
    void testQuittingAGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace, GameFeature.SingleWinner, GameFeature.Live] as Set,
                        players: [TEST_PLAYER2.md5, TEST_PLAYER3.md5]
                ),
                MediaType.APPLICATION_JSON)

        def P1 = createPlayerAPITarget(TEST_PLAYER1)

        MaskedGame game
        game = P1.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)

        assertEquals GamePhase.Challenged, game.gamePhase
        assertNotNull game.solverStates[TEST_PLAYER1.md5]
        assertNotEquals("", game.solverStates[TEST_PLAYER1.md5].workingWordPhrase)
        def P1G = createGameTarget(P1, game)

        game = quitGame(P1G)
        assertEquals(GamePhase.Quit, game.gamePhase)
        assertEquals(PlayerState.Quit, game.playerStates[TEST_PLAYER1.md5])
    }

    @Test
    void testPlayingAMultiPlayerGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace, GameFeature.SingleWinner, GameFeature.Live] as Set,
                        players: [TEST_PLAYER2.md5, TEST_PLAYER3.md5]
                ),
                MediaType.APPLICATION_JSON)

        def P1 = createPlayerAPITarget(TEST_PLAYER1)

        MaskedGame game
        game = P1.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)

        assertEquals(GamePhase.Challenged, game.gamePhase)
        assertNotNull(game.solverStates[TEST_PLAYER1.md5])
        assertEquals("_____ _____", game.solverStates[TEST_PLAYER1.md5].workingWordPhrase)
        def P1G = createGameTarget(P1, game)
        def P2G = createGameTarget(createPlayerAPITarget(TEST_PLAYER2), game)
        def P3G = createGameTarget(createPlayerAPITarget(TEST_PLAYER3), game)


        game = acceptGame(P2G)
        game = getGame(P2G)
        assertEquals(GamePhase.Challenged, game.gamePhase)
        assertNotNull game.solverStates[TEST_PLAYER2.md5]
        assertEquals("_____ _____", game.solverStates[TEST_PLAYER2.md5].workingWordPhrase)
        game = acceptGame(P3G)
        game = getGame(P3G)
        assertEquals(GamePhase.Playing, game.gamePhase)
        assertNotNull(game.solverStates[TEST_PLAYER3.md5])
        assertEquals("_____ _____", game.solverStates[TEST_PLAYER3.md5].workingWordPhrase)

        GameRepository gameRepository = applicationContext.getBean(GameRepository.class)
        Game dbLoaded1 = (Game) gameRepository.findById(new ObjectId(game.idAsString)).get()

        def phraseArray = LOREMIPSUM.toCharArray()
        int position = 0
        while (!Character.isAlphabetic((int) phraseArray[0])) {
            ++position
        }

        game = putMG(P1G.path("steal").path("0"))
        assertEquals(GamePhase.Playing, game.gamePhase)
        assertNotNull((Character) game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0))
        assertEquals(1, game.solverStates[TEST_PLAYER1.md5].penalties)
        assertEquals("", game.solverStates[TEST_PLAYER1.md5].wordPhrase)
        assertEquals(9, game.solverStates[TEST_PLAYER1.md5].penaltiesRemaining)
        assertEquals(1, game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingCountTracking])
        assertEquals(
                Arrays.asList('L'),
                game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingLetters])
        assertTrue(((List<Boolean>) game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking])[0])
        assertFalse(((List<Boolean>) game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking])[1])
        assertNotNull game.solverStates[TEST_PLAYER3.md5]
        assertEquals "", game.solverStates[TEST_PLAYER3.md5].wordPhrase

        Set<Character> chars = LOREMIPSUM.toCharArray().findAll { char it -> Character.isAlphabetic((int) it) }.collect {
            it.toUpperCase()
        } as Set
        def letter = chars.iterator().next()
        game = putMG(P2G.path("guess").path(letter.toString()))
        assertEquals GamePhase.Playing, game.gamePhase
        assertEquals([letter] as Set, game.solverStates[TEST_PLAYER2.md5].guessedLetters)

        chars.each {
            game = putMG(P3G.path("guess").path(it.toString()))
        }
        assertEquals GamePhase.RoundOver, game.gamePhase
        assertEquals(chars, game.solverStates[TEST_PLAYER3.md5].guessedLetters)
        assertEquals([(TEST_PLAYER1.md5): 0, (TEST_PLAYER2.md5): 0, (TEST_PLAYER3.md5): 1], game.playerRunningScores)
        assertEquals(TEST_PLAYER3.md5, game.featureData[GameFeature.SingleWinner])

        MaskedGame newGame = rematchGame(P2G)
        assertNotEquals(newGame.id, dbLoaded1.id)
        Game dbLoaded2 = (Game) gameRepository.findById(new ObjectId(newGame.idAsString)).get()
        dbLoaded1 = (Game) gameRepository.findById(dbLoaded1.id).get()
        assertEquals(dbLoaded1.id, dbLoaded2.previousId)
        assertNotNull(dbLoaded1.rematchTimestamp)
        assertEquals(GamePhase.Challenged, newGame.gamePhase)
        assertEquals(game.players, newGame.players)
        assertEquals(game.playerRunningScores, newGame.playerRunningScores)
        assertEquals(2, newGame.round)

        newGame = rejectGame(createGameTarget(P1, newGame))
        assertEquals(GamePhase.Declined, newGame.gamePhase)
    }

    private static MaskedGame putMG(final WebTarget webTarget) {
        return webTarget.request(MediaType.APPLICATION_JSON).put(EMPTY_PUT_POST, MaskedGame.class)
    }

}
