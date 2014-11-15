package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
class PlayerServices {
    private static final Logger logger = LoggerFactory.getLogger(PlayerServices.class)

    ThreadLocal<String> playerID = new ThreadLocal<>()

    @Autowired
    GamePlayServices gamePlayServices
    @Autowired
    NewGameHandler newGameHandler

    public PlayerServices() {
        logger.info("Created PlayerServices")
    }

    @Path("play/{gameID}")
    GamePlayServices gamePlay(@PathParam("gameID") final String gameID) {
        gamePlayServices.gameID.set(gameID)
        gamePlayServices.playerID.set(playerID.get())
        return gamePlayServices
    }

    private static class FeaturesAndPlayers {
        List<String> players
        Set<GameFeature> features
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    MaskedGame createNewGame(
            FeaturesAndPlayers featuresAndPlayers
    ) {
        newGameHandler.handleCreateNewGame(playerID.get(), featuresAndPlayers.players, featuresAndPlayers.features)
    }

}
