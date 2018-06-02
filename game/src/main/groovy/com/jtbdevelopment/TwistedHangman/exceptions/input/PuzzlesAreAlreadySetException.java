package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 7:06 PM
 */
public class PuzzlesAreAlreadySetException extends GameInputException {

    private static final String ERROR = "You have already set your puzzles.";

    public PuzzlesAreAlreadySetException() {
        super(ERROR);
    }
}
