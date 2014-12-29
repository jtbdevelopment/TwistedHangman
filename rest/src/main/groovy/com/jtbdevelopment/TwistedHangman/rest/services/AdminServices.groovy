package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/27/2014
 * Time: 6:34 PM
 */
@Component
@CompileStatic
@RolesAllowed([PlayerRoles.ADMIN])
class AdminServices {
    public static final int DEFAULT_PAGE = 0
    public static final int DEFAULT_PAGE_SIZE = 500
    @Autowired
    PlayerRepository playerRepository

    ThreadLocal<ObjectId> playerID = new ThreadLocal<>()

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Set<Player> playersToSimulate(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        return playerRepository.findAll(new PageRequest(
                page ?: DEFAULT_PAGE,
                pageSize ?: DEFAULT_PAGE_SIZE,
                Sort.Direction.ASC,
                'displayName')).toList() as Set
    }

    @PUT
    @Path("{playerID}")
    @Produces(MediaType.APPLICATION_JSON)
    Object switchEffectiveUser(@PathParam("playerID") final String effectivePlayerID) {
        Player p = playerRepository.findOne(new ObjectId(effectivePlayerID));
        if (p != null) {
            ((SessionUserInfo) SecurityContextHolder.context.authentication.principal).effectiveUser = p;
            return p;
        }
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build()
    }
}
