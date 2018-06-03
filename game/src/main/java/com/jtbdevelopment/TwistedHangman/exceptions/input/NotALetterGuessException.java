package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 6:59 PM
 */
public class NotALetterGuessException extends GameInputException {

    public static final String NOT_A_LETTER_ERROR = "Guess is not a letter.";

    public NotALetterGuessException() {
        super(NOT_A_LETTER_ERROR);
    }
}
