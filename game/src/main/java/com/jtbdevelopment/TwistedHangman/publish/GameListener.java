package com.jtbdevelopment.TwistedHangman.publish;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.gamecore.players.Player;

/**
 * Date: 12/8/14
 * Time: 6:33 PM
 *
 * TODO - publish between instances of servers so players on different servers get notified
 *
 */
public interface GameListener {
    public void gameChanged(final Game game, final Player initiatingPlayer);
}
