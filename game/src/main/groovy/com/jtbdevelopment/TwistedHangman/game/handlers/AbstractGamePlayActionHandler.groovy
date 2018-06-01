package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.exceptions.input.PlayerOutOfTurnException
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.Game
import com.jtbdevelopment.games.state.masking.GameMasker
import com.jtbdevelopment.games.state.transition.GameTransitionEngine
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * Date: 11/28/14
 * Time: 2:48 PM
 */
@CompileStatic
abstract class AbstractGamePlayActionHandler<T> extends AbstractPlayerRotatingGameActionHandler<T> {

    AbstractGamePlayActionHandler(
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractGameRepository<ObjectId, GameFeature, com.jtbdevelopment.TwistedHangman.game.state.Game> gameRepository,
            final GameTransitionEngine<com.jtbdevelopment.TwistedHangman.game.state.Game> transitionEngine,
            final GamePublisher<com.jtbdevelopment.TwistedHangman.game.state.Game, MongoPlayer> gamePublisher,
            final GameEligibilityTracker gameTracker,
            final GameMasker<ObjectId, com.jtbdevelopment.TwistedHangman.game.state.Game, MaskedGame> gameMasker) {
        super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker, gameMasker)
    }

    @Override
    protected void validatePlayerForGame(final Game game, final Player player) {
        super.validatePlayerForGame(game, player)
        if (game.features.contains(GameFeature.TurnBased)) {
            if (game.featureData[GameFeature.TurnBased] != player.id) {
                throw new PlayerOutOfTurnException()
            }
        }
    }

}
