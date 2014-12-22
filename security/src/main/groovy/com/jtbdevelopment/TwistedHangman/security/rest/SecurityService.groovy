package com.jtbdevelopment.TwistedHangman.security.rest

import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.PlayerRoles
import com.jtbdevelopment.TwistedHangman.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 12/14/14
 * Time: 7:54 PM
 *
 */
@Path("security")
@RolesAllowed(PlayerRoles.PLAYER)
@Component
@CompileStatic
class SecurityService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Player getSessionPlayer() {
        return ((SessionUserInfo) SecurityContextHolder.context.authentication.principal).sessionUser
    }
}
