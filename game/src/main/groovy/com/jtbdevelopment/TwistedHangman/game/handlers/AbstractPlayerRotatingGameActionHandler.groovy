package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.masking.GameMasker
import com.jtbdevelopment.games.state.transition.GameTransitionEngine
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
@CompileStatic
abstract class AbstractPlayerRotatingGameActionHandler<T> extends
        AbstractGameActionHandler<T,
                ObjectId,
                GameFeature,
                Game,
                MaskedGame,
                MongoPlayer> {

    AbstractPlayerRotatingGameActionHandler(
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
            final GameTransitionEngine<Game> transitionEngine,
            final GamePublisher<Game, MongoPlayer> gamePublisher,
            final GameEligibilityTracker gameTracker,
            final GameMasker<ObjectId, Game, MaskedGame> gameMasker) {
        super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker, gameMasker)
    }

    @Override
    protected Game rotateTurnBasedGame(
            final Game game) {
        if (game.gamePhase == GamePhase.Playing && game.features.contains(GameFeature.TurnBased)) {
            rotateOnePlayer(game)

            if (game.featureData[GameFeature.TurnBased] == game.wordPhraseSetter) {
                rotateOnePlayer(game)
            }
        }
        game
    }

    private void rotateOnePlayer(final Game game) {
        int index = game.players.findIndexOf { Player<ObjectId> p -> p.id == game.featureData[GameFeature.TurnBased] } + 1
        if (index >= game.players.size()) {
            index = 0
        }
        game.featureData[GameFeature.TurnBased] = game.players[index].id
    }

}
