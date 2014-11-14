package com.jtbdevelopment.TwistedHangman.exceptions.system;

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanSystemException;

/**
 * Date: 11/8/2014
 * Time: 4:14 PM
 */
public class FailedToFindPlayersException extends TwistedHangmanSystemException {
    public final static String VALID_PLAYERS = "Not all players in this game are valid anymore.";

    public FailedToFindPlayersException() {
        super(VALID_PLAYERS);
    }
}
