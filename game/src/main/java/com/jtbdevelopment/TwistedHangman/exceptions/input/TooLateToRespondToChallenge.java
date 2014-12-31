package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 11/9/2014
 * Time: 6:47 PM
 */
public class TooLateToRespondToChallenge extends GameInputException {
    public static final String ERROR = "This game is no longer in challenge mode.";

    public TooLateToRespondToChallenge() {
        super(ERROR);
    }
}
