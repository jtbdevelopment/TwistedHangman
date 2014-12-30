package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.gamecore.exceptions.GameInputException;

/**
 * Date: 11/10/14
 * Time: 9:22 PM
 */
public class PlayerNotSolvingAPuzzleException extends GameInputException {
    public static final String ERROR = "Player is not one of the solvers in this game.";

    public PlayerNotSolvingAPuzzleException() {
        super(ERROR);
    }
}
