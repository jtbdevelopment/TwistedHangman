package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 7:07 PM
 */
class StealingNegativePositionException extends GameInputException {
    static final String NEGATIVE_POSITION_ERROR = "Can't steal before beginning of word/phrase."

    StealingNegativePositionException() {
        super(NEGATIVE_POSITION_ERROR)
    }
}
