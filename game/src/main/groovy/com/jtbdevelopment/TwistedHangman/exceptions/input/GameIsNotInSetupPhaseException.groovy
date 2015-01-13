package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 6:53 PM
 */
class GameIsNotInSetupPhaseException extends GameInputException {
    static final String ERROR = "Can't set puzzle when game is not in setup phase."

    GameIsNotInSetupPhaseException() {
        super(ERROR)
    }
}
