package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/10/14
 * Time: 9:32 PM
 */
@Component
@CompileStatic
class StealLetterHandler extends AbstractGameActionHandler<Integer> {
    @Autowired
    ThievingHangmanGameActions gameActions

    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Integer param) {
        if (game.gamePhase != Game.GamePhase.Playing) {
            throw new GameIsNotInPlayModeException()
        }

        IndividualGameState state = game.solverStates[player]
        if (state) {
            gameActions.stealLetter(state, param.intValue())
        } else {
            throw new PlayerNotSolvingAPuzzleException()
        }
        game
    }
}
