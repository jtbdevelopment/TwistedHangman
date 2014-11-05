package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game

/**
 * Date: 11/4/14
 * Time: 6:43 AM
 */

public interface GameValidator {
    boolean validateGame(final Game game)
}