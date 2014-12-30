package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/5/14
 * Time: 6:37 AM
 */
public class GameOverException extends GameInputException {
    public static final String GAME_OVER_ERROR = "Game is already over.";

    public GameOverException() {
        super(GAME_OVER_ERROR);
    }
}
