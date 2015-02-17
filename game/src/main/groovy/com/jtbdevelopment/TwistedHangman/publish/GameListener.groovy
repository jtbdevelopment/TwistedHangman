package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:56 AM
 */
@CompileStatic
interface GameListener {
    void gameChanged(final Game game, final Player initiatingPlayer, final boolean initiatingServer)
}