package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
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

/**
 * Date: 11/15/2014
 * Time: 3:29 PM
 */
class TwistedHangmanIntegration extends AbstractGameIntegration {
    public static final String LOREMIPSUM = 'Lorem ipsum'
    public static final Entity EMPTY_PUT_POST = Entity.entity("", MediaType.TEXT_PLAIN)

    @BeforeClass
    static void createPuzzle() {
        PreMadePuzzle puzzle = new PreMadePuzzle()
        puzzle.category = 'PHRASE'
        puzzle.wordPhrase = LOREMIPSUM
        puzzle.source = 'test'
        applicationContext.getBean(PreMadePuzzleRepository.class).save(puzzle)
    }

    @Test
    void testGetFeatures() {
        def client = createConnection(TEST_PLAYER2)
        def features = client
                .target(API_URI)
                .path("features")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<Map<String, String>>() {
        })
        assert features == [
                "Thieving"               : GameFeature.Thieving.description,
                "Live"       : GameFeature.Live.description,
                "TurnBased"              : GameFeature.TurnBased.description,
                "Head2Head"  : GameFeature.Head2Head.description,
                "SystemPuzzles"          : GameFeature.SystemPuzzles.description,
                "AlternatingPuzzleSetter": GameFeature.AlternatingPuzzleSetter.description,
                "AllComplete": GameFeature.AllComplete.description,
                "SingleWinner"           : GameFeature.SingleWinner.description,
                "DrawGallows"            : GameFeature.DrawGallows.description,
                "DrawFace"               : GameFeature.DrawFace.description
        ]
    }

    @Test
    void testQuittingAGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace, GameFeature.SingleWinner, GameFeature.Live] as Set,
                        players: [TEST_PLAYER2.md5, TEST_PLAYER3.md5]
                ),
                MediaType.APPLICATION_JSON)

        def P1 = createConnection(TEST_PLAYER1).target(PLAYER_API)

        MaskedGame game
        game = P1.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)

        assert game.gamePhase == GamePhase.Challenged
        assert game.solverStates[TEST_PLAYER1.md5] != null
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase != ""
        def P1G = P1.path("game").path(game.idAsString)

        game = putMG(P1G.path("quit"))
        assert game.gamePhase == GamePhase.Quit
        assert game.playerStates[TEST_PLAYER1.md5] == PlayerState.Quit
    }

    @Test
    void testPlayingAMultiPlayerGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace, GameFeature.SingleWinner, GameFeature.Live] as Set,
                        players: [TEST_PLAYER2.md5, TEST_PLAYER3.md5]
                ),
                MediaType.APPLICATION_JSON)

        def P1 = createConnection(TEST_PLAYER1).target(PLAYER_API)
        def P2 = createConnection(TEST_PLAYER2).target(PLAYER_API)
        def P3 = createConnection(TEST_PLAYER3).target(PLAYER_API)

        MaskedGame game
        game = P1.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)

        assert game.gamePhase == GamePhase.Challenged
        assert game.solverStates[TEST_PLAYER1.md5] != null
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase == "_____ _____"
        def P1G = P1.path("game").path(game.idAsString)
        def P2G = P2.path("game").path(game.idAsString)
        def P3G = P3.path("game").path(game.idAsString)


        game = putMG(P2G.path("accept"))
        game = P2G.request(MediaType.APPLICATION_JSON).get(MaskedGame.class)
        assert game.gamePhase == GamePhase.Challenged
        assert game.solverStates[TEST_PLAYER2.md5] != null
        assert game.solverStates[TEST_PLAYER2.md5].workingWordPhrase == "_____ _____"
        game = putMG(P3G.path("accept"))
        game = P3G.request(MediaType.APPLICATION_JSON).get(MaskedGame.class)
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER3.md5] != null
        assert game.solverStates[TEST_PLAYER3.md5].workingWordPhrase == "_____ _____"

        GameRepository gameRepository = applicationContext.getBean(GameRepository.class)
        Game dbLoaded1 = (Game) gameRepository.findOne(new ObjectId(game.idAsString))

        def phraseArray = LOREMIPSUM.toCharArray()
        int position = 0
        while (!Character.isAlphabetic((int) phraseArray[0])) {
            ++position
        }

        game = putMG(P1G.path("steal").path("0"))
        assert game.gamePhase == GamePhase.Playing
        assert ((Character) game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0))
        assert game.solverStates[TEST_PLAYER1.md5].penalties == 1
        assert game.solverStates[TEST_PLAYER1.md5].wordPhrase == ""
        assert game.solverStates[TEST_PLAYER1.md5].penaltiesRemaining == 9
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingCountTracking] == 1
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingLetters] == [game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0)]
        assert ((Object[]) game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking])[0] == true
        assert ((Object[]) game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking])[1] == false
        assert game.solverStates[TEST_PLAYER3.md5]
        assert game.solverStates[TEST_PLAYER3.md5].wordPhrase == ""

        Set<Character> chars = LOREMIPSUM.toCharArray().findAll { char it -> Character.isAlphabetic((int) it) }.collect {
            it.toUpperCase()
        } as Set
        def letter = chars.iterator().next()
        game = putMG(P2G.path("guess").path(letter.toString()))
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER2.md5].guessedLetters == [letter] as Set

        chars.each {
            game = putMG(P3G.path("guess").path(it.toString()))
        }
        assert game.gamePhase == GamePhase.RoundOver
        assert game.solverStates[TEST_PLAYER3.md5].guessedLetters == chars
        assert game.playerRunningScores == [(TEST_PLAYER1.md5): 0, (TEST_PLAYER2.md5): 0, (TEST_PLAYER3.md5): 1]
        assert game.featureData[GameFeature.SingleWinner] == TEST_PLAYER3.md5

        MaskedGame newGame = putMG(P2G.path("rematch"))
        assert newGame.id != dbLoaded1.id
        Game dbLoaded2 = (Game) gameRepository.findOne(new ObjectId(newGame.idAsString))
        dbLoaded1 = (Game) gameRepository.findOne(dbLoaded1.id)
        assert dbLoaded2.previousId == dbLoaded1.id
        assert dbLoaded1.rematchTimestamp != null
        assert newGame.gamePhase == GamePhase.Challenged
        assert newGame.players == game.players
        assert newGame.playerRunningScores == game.playerRunningScores
        assert newGame.round == 2


        GenericType<List<MaskedGame>> type = new GenericType<List<MaskedGame>>() {}
        List<MaskedGame> games = P1.path("games").request(MediaType.APPLICATION_JSON).get(type)
        assert games.size() >= 2 // other tests make this ambiguous
        assert games.find { MaskedGame g -> g.id == dbLoaded1.idAsString }
        assert games.find { MaskedGame g -> g.id == dbLoaded2.idAsString }

        newGame = putMG(P1.path("game").path(newGame.idAsString).path("reject"))
        assert newGame.gamePhase == GamePhase.Declined
    }

    private static MaskedGame putMG(final WebTarget webTarget) {
        return webTarget.request(MediaType.APPLICATION_JSON).put(EMPTY_PUT_POST, MaskedGame.class)
    }
}
