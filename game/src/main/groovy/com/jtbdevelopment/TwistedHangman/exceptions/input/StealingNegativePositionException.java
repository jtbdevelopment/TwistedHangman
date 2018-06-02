package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 1/13/15 Time: 7:07 PM
 */
public class StealingNegativePositionException extends GameInputException {

    public static final String NEGATIVE_POSITION_ERROR = "Can't steal before beginning of word/phrase.";

    public StealingNegativePositionException() {
        super(NEGATIVE_POSITION_ERROR);
    }
}
