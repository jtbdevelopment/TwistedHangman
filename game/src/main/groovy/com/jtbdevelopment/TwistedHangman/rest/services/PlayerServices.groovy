package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.dao.StringToIDConverter
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.AbstractMultiPlayerServices
import com.jtbdevelopment.games.rest.handlers.NewGameHandler
import com.jtbdevelopment.games.rest.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.games.rest.services.AbstractAdminServices
import com.jtbdevelopment.games.rest.services.AbstractGameServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
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
class PlayerServices extends AbstractMultiPlayerServices<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> {

    private final NewGameHandler newGameHandler

    protected PlayerServices(
            final AbstractGameServices<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> gamePlayServices,
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractAdminServices<ObjectId, GameFeature, Game, MongoPlayer> adminServices,
            final StringToIDConverter<ObjectId> stringToIDConverter,
            final PlayerGamesFinderHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> playerGamesFinderHandler,
            final NewGameHandler newGameHandler) {
        super(gamePlayServices, playerRepository, adminServices, stringToIDConverter, playerGamesFinderHandler)
        this.newGameHandler = newGameHandler
    }

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
