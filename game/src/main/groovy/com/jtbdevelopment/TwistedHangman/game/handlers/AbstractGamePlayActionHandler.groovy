package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerOutOfTurnException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic

/**
 * Date: 11/28/14
 * Time: 2:48 PM
 */
@CompileStatic
abstract class AbstractGamePlayActionHandler<T> extends AbstractGameActionHandler<T> {
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
