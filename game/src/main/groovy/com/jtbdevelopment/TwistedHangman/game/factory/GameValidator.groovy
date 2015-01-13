package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:12 AM
 */
@CompileStatic
interface GameValidator {
    public abstract boolean validateGame(final Game game)

    public abstract String errorMessage()
}