package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 11/5/14
 * Time: 6:44 AM
 */
public class NotALetterGuessException extends GameInputException {
    public static final String NOT_A_LETTER_ERROR = "Guess is not a letter.";

    public NotALetterGuessException() {
        super(NOT_A_LETTER_ERROR);
    }
}
