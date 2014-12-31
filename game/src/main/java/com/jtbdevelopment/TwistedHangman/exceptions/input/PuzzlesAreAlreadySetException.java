package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 11/10/14
 * Time: 6:41 AM
 */
public class PuzzlesAreAlreadySetException extends GameInputException {
    public static final String ERROR = "You have already set your puzzles.";

    public PuzzlesAreAlreadySetException() {
        super(ERROR);
    }
}
