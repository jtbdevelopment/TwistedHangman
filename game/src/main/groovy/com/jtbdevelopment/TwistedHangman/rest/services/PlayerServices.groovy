package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.rest.services.AbstractPlayerServices
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class PlayerServices extends AbstractPlayerServices<ObjectId> {

    @Autowired
    NewGameHandler newGameHandler
    @Autowired
    PlayerGamesFinderHandler playerGamesFinderHandler

    static class FeaturesAndPlayers {
        List<String> players    // md5 list
        Set<GameFeature> features
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    MaskedMultiPlayerGame createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
        newGameHandler.handleCreateNewGame((ObjectId) playerID.get(), featuresAndPlayers.players, featuresAndPlayers.features)
    }

    @GET
    @Path("games")
    @Produces(MediaType.APPLICATION_JSON)
    public List gamesForPlayer() {
        playerGamesFinderHandler.findGames((ObjectId) playerID.get())
    }
}
