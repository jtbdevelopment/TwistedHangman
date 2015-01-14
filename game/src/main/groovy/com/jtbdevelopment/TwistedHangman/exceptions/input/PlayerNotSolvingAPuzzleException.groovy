package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 7:06 PM
 */
class PlayerNotSolvingAPuzzleException extends GameInputException {
    static final String ERROR = 'Player is not one of the solvers in this game.'

    PlayerNotSolvingAPuzzleException() {
        super(ERROR)
    }
}
