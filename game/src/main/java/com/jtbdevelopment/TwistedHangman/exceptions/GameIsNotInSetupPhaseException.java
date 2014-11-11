package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/10/14
 * Time: 9:10 PM
 */
public class GameIsNotInSetupPhaseException extends TwistedHangmanInputException {
    public static final String ERROR = "Can't set puzzle when game is not in setup phase.";

    public GameIsNotInSetupPhaseException() {
        super(ERROR);
    }
}
