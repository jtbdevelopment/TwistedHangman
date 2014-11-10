package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:37 AM
 */
public class GameOverException extends TwistedHangmanInputException {
    public static final String GAME_OVER_ERROR = "Game is already over.";

    public GameOverException() {
        super(GAME_OVER_ERROR);
    }
}
