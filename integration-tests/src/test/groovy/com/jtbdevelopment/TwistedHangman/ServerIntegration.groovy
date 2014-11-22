package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.rest.GrizzlyServerBuilder
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerGatewayService
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
import org.glassfish.grizzly.http.server.HttpServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.context.ApplicationContext

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriBuilder

/**
 * Date: 11/15/2014
 * Time: 3:29 PM
 */
class ServerIntegration {
    private static HttpServer SERVER;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/api").port(8998).build();
    private static final URI PLAYERS_URI = BASE_URI.resolve("api/player")

    static Player TEST_PLAYER1 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER1", disabled: false, displayName: "TEST PLAYER1")
    static Player TEST_PLAYER2 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER2", disabled: false, displayName: "TEST PLAYER2")
    static Player TEST_PLAYER3 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER3", disabled: false, displayName: "TEST PLAYER3")

    static ApplicationContext applicationContext
    public static final EMPTY_PUT_POST = Entity.entity("", MediaType.TEXT_PLAIN)

    @BeforeClass
    public static void initialize() {
        SERVER = GrizzlyServerBuilder.makeServer(BASE_URI, "spring-context-integration.xml")
        assert applicationContext != null
        PlayerRepository playerRepository = applicationContext.getBean(PlayerRepository.class)
        playerRepository.delete(TEST_PLAYER1.id)
        playerRepository.delete(TEST_PLAYER2.id)
        playerRepository.delete(TEST_PLAYER3.id)
        TEST_PLAYER1 = playerRepository.save(TEST_PLAYER1)
        TEST_PLAYER2 = playerRepository.save(TEST_PLAYER2)
        TEST_PLAYER3 = playerRepository.save(TEST_PLAYER3)
    }

    @AfterClass
    public static void tearDown() {
        GameRepository gameRepository = applicationContext.getBean(GameRepository.class)
        [TEST_PLAYER1, TEST_PLAYER2, TEST_PLAYER3].each {
            Player p ->
                gameRepository.findByPlayersId(p.id).each {
                    gameRepository.delete(it)
                }
        }
        PlayerRepository playerRepository = applicationContext.getBean(PlayerRepository.class)
        playerRepository.delete(TEST_PLAYER1.id)
        playerRepository.delete(TEST_PLAYER2.id)
        playerRepository.delete(TEST_PLAYER3.id)
        SERVER.shutdownNow()
    }

    @Test
    void testPing() {
        String response = ClientBuilder
                .newClient()
                .target(PLAYERS_URI)
                .path("ping")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class)
        assert PlayerGatewayService.PING_RESULT == response
    }

    @Test
    void testGetCurrentPlayer() {
        def p = ClientBuilder.newClient().target(PLAYERS_URI).path(TEST_PLAYER1.id).request(MediaType.APPLICATION_JSON).get(Player.class);
        assert p.id == TEST_PLAYER1.id
        assert p.disabled == TEST_PLAYER1.disabled
        assert p.displayName == TEST_PLAYER1.displayName
        assert p.md5 == TEST_PLAYER1.md5
    }

    @Test
    void testGetFeatures() {
        def features = ClientBuilder.newClient()
                .target(PLAYERS_URI)
                .path("features")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<Map<String, String>>() {
        })
        assert features == [
                "Thieving"               : "Stealing A Puzzle Letter Allowed.",
                "TurnBased"              : "Turn-based guessing.",
                "SystemPuzzles"          : "Twisted Hangman provide the puzzles.",
                "AlternatingPuzzleSetter": "Take turns setting the puzzle and watching others.",
                "SingleWinner"           : "Only one winner.",
                "DrawGallows"            : "Draw gallows too.",
                "DrawFace"               : "Draw face too."
        ]
    }

    @Test
    void testPlayingAMultiPlayerGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace, GameFeature.SingleWinner] as Set,
                        players: [TEST_PLAYER2.md5, TEST_PLAYER3.md5]
                ),
                MediaType.APPLICATION_JSON)

        def P1 = ClientBuilder
                .newClient()
                .target(PLAYERS_URI)
                .path(TEST_PLAYER1.id)
        def P2 = ClientBuilder
                .newClient()
                .target(PLAYERS_URI)
                .path(TEST_PLAYER2.id)
        def P3 = ClientBuilder
                .newClient()
                .target(PLAYERS_URI)
                .path(TEST_PLAYER3.id)

        MaskedGame game
        game = P1.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)

        assert game.gamePhase == GamePhase.Challenge
        assert game.solverStates[TEST_PLAYER1.md5] != null
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase != ""
        def P1G = P1.path("play").path(game.id)
        def P2G = P2.path("play").path(game.id)
        def P3G = P3.path("play").path(game.id)


        game = putMG(P2G.path("accept"))
        game = P2G.request(MediaType.APPLICATION_JSON).get(MaskedGame.class)
        assert game.gamePhase == GamePhase.Challenge
        assert game.solverStates[TEST_PLAYER2.md5] != null
        assert game.solverStates[TEST_PLAYER2.md5].workingWordPhrase != ""
        game = putMG(P3G.path("accept"))
        game = P3G.request(MediaType.APPLICATION_JSON).get(MaskedGame.class)
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER3.md5] != null
        assert game.solverStates[TEST_PLAYER3.md5].workingWordPhrase != ""

        game = putMG(P1G.path("steal").path("0"))
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0).letter
        assert game.solverStates[TEST_PLAYER1.md5].penalties == 1
        assert game.solverStates[TEST_PLAYER1.md5].penaltiesRemaining == 9
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingCountTracking] == 1
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][0] == true
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][1] == false

        GameRepository gameRepository = applicationContext.getBean(GameRepository.class)
        Game dbLoaded1 = gameRepository.findOne(game.id)
        String wordPhrase = dbLoaded1.solverStates[TEST_PLAYER1.id].wordPhraseString
        Set<Character> chars = wordPhrase.toCharArray().findAll { it.letter }.collect { it } as Set
        def letter = chars.iterator().next()
        game = putMG(P2G.path("guess").path(letter.toString()))
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER2.md5].guessedLetters == [letter] as Set

        chars.each {
            game = putMG(P3G.path("guess").path(it.toString()))
        }
        assert game.gamePhase == GamePhase.Rematch
        assert game.solverStates[TEST_PLAYER3.md5].guessedLetters == chars
        assert game.playerScores == [(TEST_PLAYER1.md5): 0, (TEST_PLAYER2.md5): 0, (TEST_PLAYER3.md5): 1]
        assert game.featureData[GameFeature.SingleWinner] == TEST_PLAYER3.md5

        MaskedGame newGame = putMG(P2G.path("rematch"))
        assert newGame.id != dbLoaded1.id
        Game dbLoaded2 = gameRepository.findOne(newGame.id)
        dbLoaded1 = gameRepository.findOne(dbLoaded1.id)
        assert dbLoaded2.previousId == dbLoaded1.id
        assert dbLoaded1.rematched != null
        assert newGame.gamePhase == GamePhase.Challenge
        assert newGame.players == game.players
        assert newGame.playerScores == game.playerScores


        GenericType<Collection<MaskedGame>> type = new GenericType<Collection<MaskedGame>>(Collection.class) {}
        List<MaskedGame> games = P1.path("games/Playing").request(MediaType.APPLICATION_JSON).get(type)
        assert games.empty
        games = P1.path("games/Challenge").queryParam("page", 0).queryParam("pageSize", 5).request(MediaType.APPLICATION_JSON).get(type)
        assert games.size() == 1
        assert games[0].id == newGame.id
        games = P1.path("games/Challenge").queryParam("page", 1).queryParam("pageSize", 5).request(MediaType.APPLICATION_JSON).get(type)
        assert games.size() == 0

        newGame = putMG(P1.path("play").path(newGame.id).path("reject"))
        assert newGame.gamePhase == GamePhase.Declined
    }

    private static MaskedGame putMG(WebTarget webTarget) {
        return webTarget.request(MediaType.APPLICATION_JSON).put(EMPTY_PUT_POST, MaskedGame.class)
    }
}
