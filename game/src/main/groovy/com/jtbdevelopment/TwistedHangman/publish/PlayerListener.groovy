package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.games.players.Player

/**
 * Date: 2/5/15
 * Time: 9:45 PM
 */
interface PlayerListener {
    void playerChanged(final Player player)

    void allPlayersChanged()
}