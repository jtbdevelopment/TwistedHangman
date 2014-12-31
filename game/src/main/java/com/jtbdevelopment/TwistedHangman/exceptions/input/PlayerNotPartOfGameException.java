package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 11/9/2014
 * Time: 6:38 PM
 */
public class PlayerNotPartOfGameException extends GameInputException {
    public static final String MESSAGE = "Player trying to act on a game they are not part of.";

    public PlayerNotPartOfGameException() {
        super(MESSAGE);
    }
}
