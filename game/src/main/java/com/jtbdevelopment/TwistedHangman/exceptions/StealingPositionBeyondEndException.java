package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:49 AM
 */
public class StealingPositionBeyondEndException extends TwistedHangmanInputException {
    public static final String POSITION_BEYOND_END_ERROR = "Can't steal letter after the end of the word/phrase.";

    public StealingPositionBeyondEndException() {
        super(POSITION_BEYOND_END_ERROR);
    }
}
