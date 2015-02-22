package com.jtbdevelopment.TwistedHangman.publish.cluster

import com.jtbdevelopment.TwistedHangman.publish.GameListener
import com.jtbdevelopment.TwistedHangman.publish.PlayerListener
import com.jtbdevelopment.games.games.Game
import com.jtbdevelopment.games.players.Player
import org.springframework.stereotype.Component

/**
 * Date: 2/17/15
 * Time: 7:10 AM
 */
@Component
class UpdatesToClusterPublisher implements GameListener, PlayerListener {
    @Override
    void gameChanged(final Game game, final Player initiatingPlayer, final boolean initiatingServer) {
        if (initiatingServer) {
            //  TODO - publish
        }
    }

    @Override
    void playerChanged(final Player player, final boolean initiatingServer) {
        if (initiatingServer) {
            //  TODO - publish
        }
    }

    @Override
    void allPlayersChanged(final boolean initiatingServer) {
        if (initiatingServer) {
            //  TODO - publish
        }
    }
}