package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/12/14
 * Time: 6:42 AM
 */
public class PlayerOutOfTurnException extends TwistedHangmanInputException {
    public static final String ERROR = "Player is playing out of turn.";

    public PlayerOutOfTurnException() {
        super(ERROR);
    }
}
