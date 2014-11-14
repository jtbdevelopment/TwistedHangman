package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanInputException;

/**
 * Date: 11/5/14
 * Time: 6:46 AM
 */
public class StealingNegativePositionException extends TwistedHangmanInputException {
    public static final String NEGATIVE_POSITION_ERROR = "Can't steal before beginning of word/phrase.";

    public StealingNegativePositionException() {
        super(NEGATIVE_POSITION_ERROR);
    }
}
