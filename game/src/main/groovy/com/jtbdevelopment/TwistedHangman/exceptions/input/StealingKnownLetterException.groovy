package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 7:07 PM
 */
class StealingKnownLetterException extends GameInputException {
    static final String STEALING_KNOWN_LETTER_ERROR = "Can't steal what you already know!"

    StealingKnownLetterException() {
        super(STEALING_KNOWN_LETTER_ERROR)
    }
}
