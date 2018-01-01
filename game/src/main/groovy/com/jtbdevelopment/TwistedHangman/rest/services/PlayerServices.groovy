package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.rest.AbstractMultiPlayerServices
import com.jtbdevelopment.games.rest.handlers.NewGameHandler
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class PlayerServices extends AbstractMultiPlayerServices<ObjectId> {

    @Autowired
    NewGameHandler newGameHandler

    static class FeaturesAndPlayers {
        List<String> players    // md5 list
        Set<GameFeature> features
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    Object createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
        newGameHandler.handleCreateNewGame(
                (Serializable) playerID.get(),
                featuresAndPlayers.players,
                featuresAndPlayers.features)
    }
}
