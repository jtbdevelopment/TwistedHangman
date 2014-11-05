package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:43 AM
 */
public class LetterAlreadyGuessedException extends TwistedHangmanException {
    public static final String ALREADY_GUESSED_ERROR = "Letter previously guessed.";

    public LetterAlreadyGuessedException() {
        super(ALREADY_GUESSED_ERROR);
    }
}
