package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/27/2014
 * Time: 6:34 PM
 */
@Component
@CompileStatic
//  -------------------------------------------
//  TODO - test test test
//  -------------------------------------------
class AdminServices {
    @Autowired
    PlayerRepository playerRepository

    ThreadLocal<String> playerID = new ThreadLocal<>()

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //  -------------------------------------------
    //  TODO - review review review
    //  -------------------------------------------
    Set<Player> playersToSimulate(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        return playerRepository.findAll(new PageRequest(page ?: 0, pageSize ?: 500, Sort.Direction.ASC, 'displayName')) as Set
    }

    @PUT
    @Path("{playerID}")
    @Produces(MediaType.APPLICATION_JSON)
    Object switchEffectiveUser(@PathParam("playerID") final String effectivePlayerID) {
        Player p = playerRepository.findOne(effectivePlayerID);
        if (p != null) {
            ((SessionUserInfo) SecurityContextHolder.context.authentication.principal).effectiveUser = p;
            return p;
        }
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build()
    }
}
