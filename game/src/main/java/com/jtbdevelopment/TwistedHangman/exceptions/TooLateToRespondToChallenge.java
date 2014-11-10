package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/9/2014
 * Time: 6:47 PM
 */
public class TooLateToRespondToChallenge extends TwistedHangmanInputException {
    public static final String ERROR = "This game is no longer in challenge mode.";

    public TooLateToRespondToChallenge() {
        super(ERROR);
    }
}
