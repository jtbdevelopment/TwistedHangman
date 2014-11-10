package com.jtbdevelopment.TwistedHangman.exceptions

/**
 * Date: 11/9/2014
 * Time: 6:47 PM
 */
public class AlreadyRespondedToChallengeException extends TwistedHangmanInputException {
    public final static String ERROR = "You have already accepted/declined this match.";

    public AlreadyRespondedToChallengeException() {
        super(ERROR)
    }
}
