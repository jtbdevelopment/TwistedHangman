package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.players.PlayerRoles
import com.jtbdevelopment.TwistedHangman.security.SessionUserInfo
import groovy.transform.CompileStatic
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
    @Autowired
    AdminServices adminServices

    @Path("player")
    public Object gameServices() {
        playerServices.playerID.set(((SessionUserInfo) SecurityContextHolder.context.authentication.principal).effectiveUser.id)
        return playerServices
    }

    @Path("admin")
    @RolesAllowed([PlayerRoles.ADMIN])
    public Object adminServices() {
        adminServices.playerID.set(((SessionUserInfo) SecurityContextHolder.context.authentication.principal).sessionUser.id)
        return adminServices
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

    @GET
    @Path("phases")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<GamePhase, List<String>> phasesAndDescriptions() {
        GamePhase.values().collectEntries() {
            GamePhase it ->
                [(it): [it.description, it.groupLabel]]
        }
    }
}
