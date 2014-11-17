package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/17/14
 * Time: 6:37 AM
 */
@Component
@CompileStatic
class GameGetterHandler extends AbstractGameActionHandler<Object> {
    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Object param) {
        return game
    }

    @Override
    protected Game rotateTurnBasedGame(final Game game, final Player player) {
        return game
    }

}
