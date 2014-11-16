package com.jtbdevelopment.TwistedHangman

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerGatewayService
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.filter.LoggingFilter
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.spring.scope.RequestContextFilter
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
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

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

    @Provider
    public static class JacksonJSR310MapperProvider implements ContextResolver<ObjectMapper> {
        final ObjectMapper defaultObjectMapper

        public JacksonJSR310MapperProvider() {
            defaultObjectMapper = createDefaultMapper();
        }

        @Override
        public ObjectMapper getContext(final Class<?> type) {
            return defaultObjectMapper;
        }

        private static ObjectMapper createDefaultMapper() {
            final ObjectMapper result = new ObjectMapper();
            result.enable(SerializationFeature.INDENT_OUTPUT);
            result.registerModule(new JSR310Module())
            result.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            result.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            return result;
        }
    }

    public static class JerseyConfig extends ResourceConfig {
        public JerseyConfig() {
            register(RequestContextFilter.class);
            packages("com.jtbdevelopment.TwistedHangman");
            property("contextConfigLocation", "classpath:spring-context-integration.xml");
            register(JacksonJSR310MapperProvider.class)
            register(JacksonFeature.class);
            register(LoggingFilter.class);
        }
    }

    @BeforeClass
    public static void initialize() {
        ResourceConfig config = new JerseyConfig();
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

        def path = ClientBuilder
                .newClient()
                .target(PLAYERS_URI)
                .path(TEST_PLAYER1.id)
                .path("new")
        MaskedGame p1Game = path
                .request(MediaType.APPLICATION_JSON)
                .post(entity, MaskedGame.class)
        println p1Game
    }

    @Test
    void testY() {
        println "t2"
    }
}
