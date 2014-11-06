package com.jtbdevelopment.TwistedHangman.game.factory;

import com.jtbdevelopment.TwistedHangman.game.state.Game;

/**
 * Date: 11/4/2014
 * Time: 9:01 PM
 */
public interface IndividualGamesInitializer {
    public abstract void initializeIndividualGameStates(final Game game);
}
