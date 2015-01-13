package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 6:48 PM
 */
class GameIsNotAvailableToRematchException extends GameInputException {
    static final String ERROR = 'Game is not available for rematching.'

    GameIsNotAvailableToRematchException() {
        super(ERROR)
    }
}
