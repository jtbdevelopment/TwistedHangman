package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 7:06 PM
 */
class PuzzlesAreAlreadySetException extends GameInputException {
    static final String ERROR = 'You have already set your puzzles.'

    PuzzlesAreAlreadySetException() {
        super(ERROR)
    }
}
