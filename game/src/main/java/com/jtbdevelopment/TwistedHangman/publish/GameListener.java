package com.jtbdevelopment.TwistedHangman.publish;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.gamecore.players.PlayerInt;

import java.io.Serializable;

/**
 * Date: 12/8/14
 * Time: 6:33 PM
 * <p>
 * TODO - publish between instances of servers so players on different servers get notified
 */
public interface GameListener<ID extends Serializable> {
    public void gameChanged(final Game game, final PlayerInt<ID> initiatingPlayer);
}
