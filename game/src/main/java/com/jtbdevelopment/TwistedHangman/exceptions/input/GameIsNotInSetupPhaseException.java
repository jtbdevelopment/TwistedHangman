package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 6:53 PM
 */
public class GameIsNotInSetupPhaseException extends GameInputException {

    private static final String ERROR = "Can't set puzzle when game is not in setup phase.";

    public GameIsNotInSetupPhaseException() {
        super(ERROR);
    }
}
