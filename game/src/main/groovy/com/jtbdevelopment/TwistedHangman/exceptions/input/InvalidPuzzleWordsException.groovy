package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 6:58 PM
 */
class InvalidPuzzleWordsException extends GameInputException {
    static final String BASE_ERROR = 'Your puzzle has invalid words '
    static final String END_EROR = '.'

    InvalidPuzzleWordsException(final List<String> invalidWords) {
        super(BASE_ERROR + invalidWords + END_EROR);
    }
}
