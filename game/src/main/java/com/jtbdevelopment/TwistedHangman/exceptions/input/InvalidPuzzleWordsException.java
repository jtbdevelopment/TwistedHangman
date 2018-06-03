package com.jtbdevelopment.TwistedHangman.exceptions.input;

import com.jtbdevelopment.games.exceptions.GameInputException;
import java.util.List;

/**
 * Date: 1/13/15 Time: 6:58 PM
 */
public class InvalidPuzzleWordsException extends GameInputException {

    private static final String BASE_ERROR = "Your puzzle has invalid words ";
    private static final String END_EROR = ".";

    public InvalidPuzzleWordsException(final List<String> invalidWords) {
        super(BASE_ERROR + invalidWords + END_EROR);
    }
}
