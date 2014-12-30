package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/10/14
 * Time: 9:19 PM
 */
public class GameIsNotPossibleToQuitNowException extends GameInputException {
    public static final String ERROR = "Game is not available to quit anymore.";

    public GameIsNotPossibleToQuitNowException() {
        super(ERROR);
    }
}
