package com.jtbdevelopment.TwistedHangman.exceptions.system;

import com.jtbdevelopment.games.exceptions.GameSystemException;

/**
 * Date: 11/8/2014
 * Time: 4:14 PM
 */
public class FailedToFindGameException extends GameSystemException {
    public final static String VALID_GAME = "Was not able to load game.";

    public FailedToFindGameException() {
        super(VALID_GAME);
    }
}
