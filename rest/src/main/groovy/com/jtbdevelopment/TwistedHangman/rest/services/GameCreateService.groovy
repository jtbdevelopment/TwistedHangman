package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:59 PM
 */
@Component
@CompileStatic
class GameCreateService {
    @Autowired
    NewGameHandler newGameHandler

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    Game createNewGame(
            @FormParam("playerID") final String playerID,
            @FormParam("players") final List<String> players,
            @FormParam("features") final Set<GameFeature> gameFeatures) {
        return newGameHandler.handleCreateNewGame(playerID, players, gameFeatures)
    }
}
