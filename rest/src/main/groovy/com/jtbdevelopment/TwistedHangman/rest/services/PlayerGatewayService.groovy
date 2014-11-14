package com.jtbdevelopment.TwistedHangman.rest.services

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:36 AM
 */
@Path("player")
@Component
@CompileStatic
class PlayerGatewayService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerGatewayService.class)
    public static final String PING_RESULT = "Alive."

    @Autowired
    PlayerServices playerServices

    public PlayerGatewayService() {
        logger.info("Created PlayerGatewayService")
    }

    @Path("{playerID}")
    public PlayerServices gameServices(@PathParam("playerID") final String playerID) {
        //  TODO verify call is in fact the player
        playerServices.playerID.set(playerID)
        return playerServices
    }

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("ping")
    String ping() {
        return PING_RESULT
    }
}
