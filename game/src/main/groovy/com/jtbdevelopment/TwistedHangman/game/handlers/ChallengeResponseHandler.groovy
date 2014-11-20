package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.TooLateToRespondToChallenge
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/9/2014
 * Time: 5:27 PM
 */
@CompileStatic
@Component
class ChallengeResponseHandler extends AbstractGameActionHandler<PlayerChallengeState> {
    @Override
    protected Game handleActionInternal(final Player player, final Game game, final PlayerChallengeState param) {
        // We will at least record further ack/nacks for information
        if (game.gamePhase == GamePhase.Challenge ||
                game.gamePhase == GamePhase.Declined) {
            game.playerStates[player.id] = param         //  Players can change their mind in the server side
            return game
        } else {
            throw new TooLateToRespondToChallenge()
        }
    }
}