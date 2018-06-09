package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 6:58 PM
 */
public class LetterAlreadyGuessedException extends GameInputException {

    private static final String ALREADY_GUESSED_ERROR = "Letter previously guessed.";

    public LetterAlreadyGuessedException() {
        super(ALREADY_GUESSED_ERROR);
    }
}
