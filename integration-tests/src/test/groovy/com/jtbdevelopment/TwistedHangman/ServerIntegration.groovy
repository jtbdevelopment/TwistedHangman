package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.TwistedHangmanPlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerState
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerGatewayService
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
import com.jtbdevelopment.gamecore.players.ManualPlayer
import com.jtbdevelopment.gamecore.players.friendfinder.SourceBasedFriendFinder
import org.bson.types.ObjectId
import org.eclipse.jetty.server.Server
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.security.crypto.password.PasswordEncoder

import javax.ws.rs.client.Client
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
    private static Server SERVER;
    private static final int port = 8998;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(port).build();
    private static final URI API_URI = BASE_URI.resolve("/api")
    private static final URI PLAYER_API = BASE_URI.resolve("api/player")

    static ManualPlayer TEST_PLAYER1
    static ManualPlayer TEST_PLAYER2
    static ManualPlayer TEST_PLAYER3

    static ManualPlayer createPlayer(final String id, final String sourceId, final String displayName) {
        return new ManualPlayer(
                id: new ObjectId(id.padRight(24, "0")),
                sourceId: sourceId,
                password: passwordEncoder.encode(sourceId),
                displayName: displayName,
                disabled: false,
                verified: true
        )
    }

    static ApplicationContext applicationContext
    static PasswordEncoder passwordEncoder
    static TwistedHangmanPlayerRepository playerRepository
    public static final EMPTY_PUT_POST = Entity.entity("", MediaType.TEXT_PLAIN)

    @BeforeClass
    public static void initialize() {
        SERVER = JettyServer.makeServer(port, "spring-context-integration.xml")
        SERVER.start()

        assert applicationContext != null
        playerRepository = applicationContext.getBean(TwistedHangmanPlayerRepository.class)
        passwordEncoder = applicationContext.getBean(PasswordEncoder.class)

        TEST_PLAYER1 = createPlayer("f1234", "ITP1", "TEST PLAYER1")
        TEST_PLAYER2 = createPlayer("f2345", "ITP2", "TEST PLAYER2")
        TEST_PLAYER3 = createPlayer("f3456", "ITP3", "TEST PLAYER3")

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
            ManualPlayer p ->
                gameRepository.findByPlayersId(p.id).each {
                    gameRepository.delete(it)
                }
        }
        playerRepository.delete(TEST_PLAYER1.id)
        playerRepository.delete(TEST_PLAYER2.id)
        playerRepository.delete(TEST_PLAYER3.id)
        SERVER.stop()
    }

    @Test
    void testPing() {
        Client client = createConnection(TEST_PLAYER1)
        String response = client
                .target(API_URI)
                .path("ping")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class)
        assert PlayerGatewayService.PING_RESULT == response
    }

    @Test
    void testGetCurrentPlayer() {
        Client client = createConnection(TEST_PLAYER1)
        def p = client.target(PLAYER_API).request(MediaType.APPLICATION_JSON).get(ManualPlayer.class);
        assert p.id == TEST_PLAYER1.id
        assert p.disabled == TEST_PLAYER1.disabled
        assert p.displayName == TEST_PLAYER1.displayName
        assert p.md5 == TEST_PLAYER1.md5
    }

    @Test
    void testGetFriends() {
        Client client = createConnection(TEST_PLAYER1)
        WebTarget path = client
                .target(PLAYER_API)
                .path("friends")
        Map<String, Object> friends = path
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<Map<String, Object>>() {});
        Map<String, String> players = friends[SourceBasedFriendFinder.MASKED_FRIENDS_KEY]
        assert players[TEST_PLAYER2.md5] == TEST_PLAYER2.displayName
        assert players[TEST_PLAYER3.md5] == TEST_PLAYER3.displayName
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
    void testGetPhases() {
        def client = createConnection(TEST_PLAYER3)
        def features = client
                .target(API_URI)
                .path("phases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<Map<String, List<String>>>() {
        })
        assert features == [
                "Challenged"      : [GamePhase.Challenged.description, GamePhase.Challenged.groupLabel],
                "Declined"        : [GamePhase.Declined.description, GamePhase.Declined.groupLabel],
                "Quit"            : [GamePhase.Quit.description, GamePhase.Quit.groupLabel],
                "Setup"           : [GamePhase.Setup.description, GamePhase.Setup.groupLabel],
                "RoundOver"       : [GamePhase.RoundOver.description, GamePhase.RoundOver.groupLabel],
                "NextRoundStarted": [GamePhase.NextRoundStarted.description, GamePhase.NextRoundStarted.groupLabel],
                "Playing"         : [GamePhase.Playing.description, GamePhase.Playing.groupLabel],
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
        def P1G = P1.path("game").path(game.id)

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
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase != ""
        def P1G = P1.path("game").path(game.id)
        def P2G = P2.path("game").path(game.id)
        def P3G = P3.path("game").path(game.id)


        game = putMG(P2G.path("accept"))
        game = P2G.request(MediaType.APPLICATION_JSON).get(MaskedGame.class)
        assert game.gamePhase == GamePhase.Challenged
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
        assert game.solverStates[TEST_PLAYER1.md5].wordPhrase == ""
        assert game.solverStates[TEST_PLAYER1.md5].penaltiesRemaining == 9
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingCountTracking] == 1
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingLetters] == [game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0)]
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][0] == true
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][1] == false
        assert game.solverStates[TEST_PLAYER3.md5]
        assert game.solverStates[TEST_PLAYER3.md5].wordPhrase == ""

        GameRepository gameRepository = applicationContext.getBean(GameRepository.class)
        Game dbLoaded1 = gameRepository.findOne(new ObjectId(game.id))
        String wordPhrase = dbLoaded1.solverStates[TEST_PLAYER1.id].wordPhraseString
        Set<Character> chars = wordPhrase.toCharArray().findAll { it.letter }.collect { it } as Set
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
        Game dbLoaded2 = gameRepository.findOne(new ObjectId(newGame.id))
        dbLoaded1 = gameRepository.findOne(dbLoaded1.id)
        assert dbLoaded2.previousId == dbLoaded1.id
        assert dbLoaded1.rematched != null
        assert newGame.gamePhase == GamePhase.Challenged
        assert newGame.players == game.players
        assert newGame.playerRunningScores == game.playerRunningScores
        assert newGame.round == 2


        GenericType<List<MaskedGame>> type = new GenericType<List<MaskedGame>>() {}
        List<MaskedGame> games = P1.path("games").request(MediaType.APPLICATION_JSON).get(type)
        assert games.size() >= 2 // other tests make this ambiguous
        assert games.find { MaskedGame g -> g.id == dbLoaded1.id.toHexString() }
        assert games.find { MaskedGame g -> g.id == dbLoaded2.id.toHexString() }

        newGame = putMG(P1.path("game").path(newGame.id).path("reject"))
        assert newGame.gamePhase == GamePhase.Declined
    }

    private static MaskedGame putMG(WebTarget webTarget) {
        return webTarget.request(MediaType.APPLICATION_JSON).put(EMPTY_PUT_POST, MaskedGame.class)
    }

    private static Client createConnection(ManualPlayer p) {
        Client client = ClientBuilder.newClient()
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(p.sourceId, p.sourceId)
        client.register(feature)
        client
    }
}
