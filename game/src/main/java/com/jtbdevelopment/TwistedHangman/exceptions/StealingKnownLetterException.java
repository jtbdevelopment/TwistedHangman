package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:47 AM
 */
public class StealingKnownLetterException extends TwistedHangmanInputException {
    public static final String STEALING_KNOWN_LETTER_ERROR = "Can't steal what you already know!";

    public StealingKnownLetterException() {
        super(STEALING_KNOWN_LETTER_ERROR);
    }
}
