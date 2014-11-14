package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanInputException;

/**
 * Date: 11/5/14
 * Time: 6:44 AM
 */
public class NotALetterGuessException extends TwistedHangmanInputException {
    public static final String NOT_A_LETTER_ERROR = "Guess is not a letter.";

    public NotALetterGuessException() {
        super(NOT_A_LETTER_ERROR);
    }
}
