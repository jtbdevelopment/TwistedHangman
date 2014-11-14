package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanInputException;

/**
 * Date: 11/5/14
 * Time: 6:43 AM
 */
public class LetterAlreadyGuessedException extends TwistedHangmanInputException {
    public static final String ALREADY_GUESSED_ERROR = "Letter previously guessed.";

    public LetterAlreadyGuessedException() {
        super(ALREADY_GUESSED_ERROR);
    }
}
