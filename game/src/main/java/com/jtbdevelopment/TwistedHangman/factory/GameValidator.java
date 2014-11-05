package com.jtbdevelopment.TwistedHangman.factory;

import com.jtbdevelopment.TwistedHangman.game.state.Game;

/**
 * Date: 11/4/14
 * Time: 6:43 AM
 */
public interface GameValidator {
    public abstract boolean validateGame(final Game game);

    public abstract String errorMessage();
}
