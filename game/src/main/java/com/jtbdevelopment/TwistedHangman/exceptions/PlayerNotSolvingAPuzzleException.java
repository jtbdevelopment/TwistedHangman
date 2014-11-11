package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/10/14
 * Time: 9:22 PM
 */
public class PlayerNotSolvingAPuzzleException extends TwistedHangmanInputException {
    public static final String ERROR = "Player is not one of the solvers in this game.";

    public PlayerNotSolvingAPuzzleException() {
        super(ERROR);
    }
}
