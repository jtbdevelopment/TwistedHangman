package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

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
}
