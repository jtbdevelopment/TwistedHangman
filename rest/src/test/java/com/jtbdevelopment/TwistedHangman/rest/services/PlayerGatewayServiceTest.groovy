package com.jtbdevelopment.TwistedHangman.rest.services

import groovy.transform.TypeChecked
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 11:28 AM
 */
class PlayerGatewayServiceTest extends GroovyTestCase {
    PlayerGatewayService playerGatewayService = new PlayerGatewayService()

    void testPing() {
        assert PlayerGatewayService.PING_RESULT == playerGatewayService.ping()
    }

    void testPingAnnotations() {
        def ping = PlayerGatewayService.getMethod("ping", [] as Class[])
        assert (ping.annotations.size() == 3 ||
                (ping.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && ping.annotations.size() == 4)
        )
        assert ping.isAnnotationPresent(GET.class)
        assert ping.isAnnotationPresent(Produces.class)
        assert ping.getAnnotation(Produces.class).value() == [MediaType.TEXT_PLAIN]
        assert ping.isAnnotationPresent(Path.class)
        assert ping.getAnnotation(Path.class).value() == "ping"
    }

    void testValidPlayer() {
        PlayerServices services = [playerID: new ThreadLocal<String>()] as PlayerServices
        playerGatewayService.playerServices = services

        def APLAYER = "APLAYER"
        assert services.is(playerGatewayService.gameServices(APLAYER))
        assert services.playerID.get() == APLAYER
    }

    void testNullPlayer() {
        playerGatewayService.playerServices = null

        OutboundJaxrsResponse resp = playerGatewayService.gameServices(null)
        assert resp.status == javax.ws.rs.core.Response.Status.BAD_REQUEST.statusCode
        assert resp.entity == "Missing player identity"
    }

    void testEmptyPlayer() {
        playerGatewayService.playerServices = null

        OutboundJaxrsResponse resp = playerGatewayService.gameServices("  ")
        assert resp.status == javax.ws.rs.core.Response.Status.BAD_REQUEST.statusCode
        assert resp.entity == "Missing player identity"
    }

    void testGameServicesAnnotations() {
        def gameServices = PlayerGatewayService.getMethod("gameServices", [String.class] as Class[])
        assert (gameServices.annotations.size() == 1 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 2)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "{playerID}"
        def params = gameServices.parameterAnnotations
        assert params.length == 1
        assert params[0].length == 1
        assert params[0][0].annotationType() == PathParam.class
        assert ((PathParam) params[0][0]).value() == "playerID"
    }
}

