package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/10/14
 * Time: 9:32 PM
 */
@Component
@CompileStatic
class StealLetterHandler extends AbstractGamePlayActionHandler<Integer> {
    @Autowired
    ThievingHangmanGameActions gameActions

    @Override
    protected Game handleActionInternal(final Player<ObjectId> player, final Game game, final Integer param) {
        if (game.gamePhase != GamePhase.Playing) {
            throw new GameIsNotInPlayModeException()
        }

        IndividualGameState state = game.solverStates[player.id]
        if (state) {
            gameActions.stealLetter(state, param.intValue())
        } else {
            throw new PlayerNotSolvingAPuzzleException()
        }
        game
    }
}
