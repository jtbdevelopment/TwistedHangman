package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/5/14
 * Time: 6:49 AM
 */
public class StealingPositionBeyondEndException extends GameInputException {
    public static final String POSITION_BEYOND_END_ERROR = "Can't steal letter after the end of the word/phrase.";

    public StealingPositionBeyondEndException() {
        super(POSITION_BEYOND_END_ERROR);
    }
}
