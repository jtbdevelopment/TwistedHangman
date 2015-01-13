package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 6:42 PM
 */
class StealingOnFinalPenaltyException extends GameInputException {
    static final String CANT_STEAL_ON_FINAL_PENALTY_ERROR = "Can't steal a letter with only one penalty left."

    StealingOnFinalPenaltyException() {
        super(CANT_STEAL_ON_FINAL_PENALTY_ERROR);
    }
}
