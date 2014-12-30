package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/5/14
 * Time: 7:11 AM
 */
public class FailedToCreateValidGameException extends GameInputException {
    public static final String BASE_ERROR = "System failed to create a valid game.  ";

    public FailedToCreateValidGameException(final String s) {
        super(BASE_ERROR + s);
    }
}
