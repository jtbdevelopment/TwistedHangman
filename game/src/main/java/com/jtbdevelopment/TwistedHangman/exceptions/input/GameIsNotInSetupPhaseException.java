package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/10/14
 * Time: 9:10 PM
 */
public class GameIsNotInSetupPhaseException extends GameInputException {
    public static final String ERROR = "Can't set puzzle when game is not in setup phase.";

    public GameIsNotInSetupPhaseException() {
        super(ERROR);
    }
}
