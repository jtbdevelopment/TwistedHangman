package com.jtbdevelopment.gamecore.security.rest

import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
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

    @SuppressWarnings("GrMethodMayBeStatic")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getSessionPlayer() {
        return ((SessionUserInfo) SecurityContextHolder.context.authentication.principal).sessionUser
    }
}
