package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/10/14
 * Time: 9:16 PM
 */
@CompileStatic
@Component
class GuessLetterHandler extends AbstractPlayerRotatingGameActionHandler<Character> {
    @Autowired
    HangmanGameActions gameActions

    @Override
    protected Game handleActionInternal(
            final Player player, final Game game, final Character param) {
        if (game.gamePhase != GamePhase.Playing) {
            throw new GameIsNotInPlayModeException()
        }

        IndividualGameState state = game.solverStates[(ObjectId) player.id]
        if (state) {
            gameActions.guessLetter(state, param)
        } else {
            throw new PlayerNotSolvingAPuzzleException()
        }
        game
    }
}
