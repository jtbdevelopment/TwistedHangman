package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotPossibleToQuitNowException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.PlayerState
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/28/2014
 * Time: 7:40 PM
 */
@CompileStatic
@Component
class QuitHandler extends AbstractGameActionHandler<Object, Game> {
    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Object param) {
        if (game.gamePhase == GamePhase.NextRoundStarted ||
                game.gamePhase == GamePhase.RoundOver ||
                game.gamePhase == GamePhase.Quit ||
                game.gamePhase == GamePhase.Declined) {
            throw new GameIsNotPossibleToQuitNowException()
        }

        game.gamePhase = GamePhase.Quit
        game.playerStates[(ObjectId) player.id] = PlayerState.Quit
        game
    }
}
