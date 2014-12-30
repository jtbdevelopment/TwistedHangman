package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:36 AM
 */
@Path("/")
@Component
@CompileStatic
@RolesAllowed([PlayerRoles.PLAYER])
class PlayerGatewayService {
    public static final String PING_RESULT = "Alive."

    @Autowired
    PlayerServices playerServices

    @Path("player")
    public Object gameServices() {
        playerServices.playerID.set(((SessionUserInfo<ObjectId>) SecurityContextHolder.context.authentication.principal).effectiveUser.id)
        return playerServices
    }

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("ping")
    @SuppressWarnings("GrMethodMayBeStatic")
    String ping() {
        return PING_RESULT
    }

    @GET
    @Path("features")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("GrMethodMayBeStatic")
    public Map<GameFeature, String> featuresAndDescriptions() {
        GameFeature.values().findAll { GameFeature it -> !it.internal }.collectEntries() {
            GameFeature it ->
                [(it): it.description]
        }
    }

    @GET
    @Path("phases")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("GrMethodMayBeStatic")
    public Map<GamePhase, List<String>> phasesAndDescriptions() {
        GamePhase.values().collectEntries() {
            GamePhase it ->
                [(it): [it.description, it.groupLabel]]
        }
    }
}
