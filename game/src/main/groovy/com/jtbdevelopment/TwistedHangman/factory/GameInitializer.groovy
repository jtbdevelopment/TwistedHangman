package com.jtbdevelopment.TwistedHangman.factory

import com.jtbdevelopment.TwistedHangman.game.Game
import groovy.transform.CompileStatic

/**
 * Date: 11/4/14
 * Time: 6:44 AM
 */
@CompileStatic
public interface GameInitializer {
    public void initializeGame(final Game game)
}