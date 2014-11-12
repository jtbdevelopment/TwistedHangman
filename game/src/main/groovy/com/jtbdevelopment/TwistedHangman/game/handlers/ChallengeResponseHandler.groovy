package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.TooLateToRespondToChallenge
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/9/2014
 * Time: 5:27 PM
 */
@CompileStatic
@Component
class ChallengeResponseHandler extends AbstractGameActionHandler<Game.PlayerChallengeState> {
    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Game.PlayerChallengeState param) {
        // We will at least record further ack/nacks for information
        if (game.gamePhase == Game.GamePhase.Challenge ||
                game.gamePhase == Game.GamePhase.Declined) {
            game.playerStates[player.id] = param         //  Players can change their mind in the server side
            return game
        } else {
            throw new TooLateToRespondToChallenge()
        }
    }
}
