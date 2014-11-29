package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanInputException;

/**
 * Date: 11/10/14
 * Time: 9:19 PM
 */
public class GameIsNotPossibleToQuitNowException extends TwistedHangmanInputException {
    public static final String ERROR = "Game is not available to quit anymore.";

    public GameIsNotPossibleToQuitNowException() {
        super(ERROR);
    }
}
