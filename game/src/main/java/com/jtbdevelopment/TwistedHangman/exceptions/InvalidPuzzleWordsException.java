package com.jtbdevelopment.TwistedHangman.exceptions;

import java.util.List;

/**
 * Date: 11/10/14
 * Time: 6:41 AM
 */
public class InvalidPuzzleWordsException extends TwistedHangmanInputException {
    public static final String BASE_ERROR = "Your puzzle has invalid words ";
    public static final String END_EROR = ".";

    public InvalidPuzzleWordsException(final List<String> invalidWords) {
        super(BASE_ERROR + invalidWords + END_EROR);
    }
}
