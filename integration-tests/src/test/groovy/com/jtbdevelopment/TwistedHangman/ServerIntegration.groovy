package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.rest.config.JerseyConfig
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerGatewayService
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriBuilder

/**
 * Date: 11/15/2014
 * Time: 3:29 PM
 */
class ServerIntegration {
    private static HttpServer SERVER;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(8998).build();
    private static final URI PLAYERS_URI = BASE_URI.resolve("player")
    private static URI TEST_PLAYER1_URI;
    private static URI TEST_PLAYER2_URI;
    private static URI TEST_PLAYER3_URI;

    private
    static Player TEST_PLAYER1 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER1", disabled: false, displayName: "TEST PLAYER1")
    static Player TEST_PLAYER2 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER2", disabled: false, displayName: "TEST PLAYER2")
    static Player TEST_PLAYER3 = new Player(source: "MANUAL", id: "INTEGRATION-TEST-PLAYER3", disabled: false, displayName: "TEST PLAYER3")

    static ApplicationContext applicationContext

    @Component
    @Lazy(false)
    static class ContextListener implements ApplicationContextAware {
        @Override
        void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
            ServerIntegration.applicationContext = applicationContext
        }
    }

    @BeforeClass
    public static void initialize() {
        ResourceConfig config = new JerseyConfig("spring-context-integration.xml");
        SERVER = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, config);
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
    void testPlayingAMultiPlayerGame() {
        def entity = Entity.entity(
                new PlayerServices.FeaturesAndPlayers(
                        features: [GameFeature.SystemPuzzles, GameFeature.Thieving, GameFeature.DrawFace] as Set,
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

        def empty = Entity.entity("", MediaType.TEXT_PLAIN)

        game = P2G.path("accept").request(MediaType.APPLICATION_JSON).put(empty, MaskedGame.class)
        assert game.gamePhase == GamePhase.Challenge
        assert game.solverStates[TEST_PLAYER2.md5] != null
        assert game.solverStates[TEST_PLAYER2.md5].workingWordPhrase != ""
        game = P3G.path("accept").request(MediaType.APPLICATION_JSON).put(empty, MaskedGame.class)
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER3.md5] != null
        assert game.solverStates[TEST_PLAYER3.md5].workingWordPhrase != ""

        game = P1G.path("steal").path("0").request(MediaType.APPLICATION_JSON).put(empty, MaskedGame.class)
        assert game.gamePhase == GamePhase.Playing
        assert game.solverStates[TEST_PLAYER1.md5].workingWordPhrase.charAt(0).letter
        assert game.solverStates[TEST_PLAYER1.md5].penalties == 1
        assert game.solverStates[TEST_PLAYER1.md5].penaltiesRemaining == 9
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingCountTracking] == 1
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][0] == true
        assert game.solverStates[TEST_PLAYER1.md5].featureData[GameFeature.ThievingPositionTracking][1] == false
    }

    @Test
    void testY() {
        println "t2"
    }
}
