package com.jtbdevelopment.TwistedHangman.publish;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.players.Player;

/**
 * Date: 12/8/14
 * Time: 6:33 PM
 */
public interface GameListener {
    public void gameChanged(final Game game, final Player initiatingPlayer);
}
