package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotPossibleToQuitNowException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/28/2014
 * Time: 7:40 PM
 */
@CompileStatic
@Component
class QuitHandler extends AbstractGameActionHandler<Object> {
    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Object param) {
        if (game.gamePhase == GamePhase.Rematched ||
                game.gamePhase == GamePhase.Rematch ||
                game.gamePhase == GamePhase.Quit ||
                game.gamePhase == GamePhase.Declined) {
            throw new GameIsNotPossibleToQuitNowException()
        }

        game.gamePhase = GamePhase.Quit
        game.playerStates[player.id] = PlayerChallengeState.Quit
        game
    }
}
