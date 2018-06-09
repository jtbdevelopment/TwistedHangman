package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 7:07 PM
 */
public class StealingPositionBeyondEndException extends GameInputException {

    private static final String POSITION_BEYOND_END_ERROR = "Can't steal letter after the end of the word/phrase.";

    public StealingPositionBeyondEndException() {
        super(POSITION_BEYOND_END_ERROR);
    }
}
