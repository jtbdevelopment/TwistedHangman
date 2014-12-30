package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/11/14
 * Time: 6:52 PM
 */
public class GameIsNotAvailableToRematchException extends GameInputException {
    public static final String ERROR = "Game is not available for rematching.";

    public GameIsNotAvailableToRematchException() {
        super(ERROR);
    }
}
