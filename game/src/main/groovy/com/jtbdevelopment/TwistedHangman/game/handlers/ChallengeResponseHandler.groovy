package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.TooLateToRespondToChallengeException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/9/2014
 * Time: 5:27 PM
 */
@CompileStatic
@Component
class ChallengeResponseHandler extends AbstractPlayerRotatingGameActionHandler<PlayerState> {
    @Override
    protected boolean requiresEligibilityCheck(final PlayerState param) {
        return PlayerState.Accepted == param
    }

    @Override
    protected Game handleActionInternal(
            final Player player, final Game game, final PlayerState param) {
        // We will at least record further ack/nacks for information
        if (game.gamePhase == GamePhase.Challenged || game.gamePhase == GamePhase.Declined) {
            game.playerStates[(ObjectId) player.id] = param         //  Players can change their mind in the server side
            return game
        } else {
            throw new TooLateToRespondToChallengeException()
        }
    }
}
