package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
@CompileStatic
abstract class AbstractPlayerRotatingGameActionHandler<T> extends AbstractGameActionHandler<T, Game> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPlayerRotatingGameActionHandler.class)

    @Override
    protected Game rotateTurnBasedGame(
            final Game game) {
        if (game.gamePhase == GamePhase.Playing && game.features.contains(GameFeature.TurnBased)) {
            rotateOnePlayer(game)

            if (game.featureData[GameFeature.TurnBased] == game.wordPhraseSetter) {
                rotateOnePlayer(game);
            }
        }
        game
    }

    protected static void rotateOnePlayer(final Game game) {
        int index = game.players.findIndexOf { Player<ObjectId> p -> p.id == game.featureData[GameFeature.TurnBased] } + 1
        if (index >= game.players.size()) {
            index = 0
        }
        game.featureData[GameFeature.TurnBased] = game.players[index].id
    }

}
