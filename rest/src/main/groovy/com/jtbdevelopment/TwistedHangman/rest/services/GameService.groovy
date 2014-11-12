package com.jtbdevelopment.TwistedHangman.rest.services

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Path("games")
@Service
@CompileStatic
class GameService {
    static final String PING_RESULT = "Alive."

    @Autowired
    GameCreateService gameCreateService

    @Path("create")
    Object createGame() {
        return gameCreateService
    }

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("ping")
    String ping() {
        return PING_RESULT
    }
}
