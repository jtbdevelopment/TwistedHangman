package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/14/14
 * Time: 6:36 AM
 */
@Path("player")
@Component
@CompileStatic
class PlayerGatewayService {
    public static final String PING_RESULT = "Alive."

    @Autowired
    PlayerServices playerServices

    @Path("{playerID}")
    public Object gameServices(@PathParam("playerID") final String playerID) {
        //  TODO verify call is in fact the player
        if (StringUtils.isEmpty(playerID) || StringUtils.isEmpty(playerID.trim())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing player identity").build()
        }
        playerServices.playerID.set(playerID)
        return playerServices
    }

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("ping")
    String ping() {
        return PING_RESULT
    }

    @GET
    @Path("features")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<GameFeature, String> featuresAndDescriptions() {
        GameFeature.values().findAll { GameFeature it -> !it.internal }.collectEntries() {
            GameFeature it ->
                [(it): it.description]
        }
    }
}
