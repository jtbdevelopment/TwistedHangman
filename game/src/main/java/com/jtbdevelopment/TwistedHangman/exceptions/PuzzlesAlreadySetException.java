package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/10/14
 * Time: 6:41 AM
 */
public class PuzzlesAlreadySetException extends TwistedHangmanInputException {
    public static final String ERROR = "You have already set your puzzles.";

    public PuzzlesAlreadySetException() {
        super(ERROR);
    }
}
