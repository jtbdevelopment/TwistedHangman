package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.services.AbstractPlayerGatewayService
import com.jtbdevelopment.games.rest.services.AbstractPlayerServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:36 AM
 */
@Path("/")
@Component
@CompileStatic
class PlayerGatewayService extends AbstractPlayerGatewayService<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> {
    protected PlayerGatewayService(
            final AbstractPlayerServices<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> playerServices) {
        super(playerServices)
    }

    @GET
    @Path("features")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("GrMethodMayBeStatic")
    Map<GameFeature, String> featuresAndDescriptions() {
        GameFeature.values().findAll { GameFeature it -> !it.internal }.collectEntries() {
            GameFeature it ->
                [(it): it.description]
        }
    }

}
