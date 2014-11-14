package com.jtbdevelopment.TwistedHangman.exceptions.system;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanSystemException;

/**
 * Date: 11/8/2014
 * Time: 4:14 PM
 */
public class FailedToFindGameException extends TwistedHangmanSystemException {
    public final static String VALID_GAME = "Was not able to load game.";

    public FailedToFindGameException() {
        super(VALID_GAME);
    }
}
