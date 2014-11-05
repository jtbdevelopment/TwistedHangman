package com.jtbdevelopment.TwistedHangman.factory;

import com.jtbdevelopment.TwistedHangman.game.state.Game;

/**
 * Date: 11/4/14
 * Time: 6:44 AM
 */
public interface GameInitializer {
    public abstract void initializeGame(final Game game);
}
