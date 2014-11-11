package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/10/14
 * Time: 9:19 PM
 */
public class GameIsNotInPlayModeException extends TwistedHangmanInputException {
    public static final String ERROR = "Game is not open for playing.";

    public GameIsNotInPlayModeException() {
        super(ERROR);
    }
}
