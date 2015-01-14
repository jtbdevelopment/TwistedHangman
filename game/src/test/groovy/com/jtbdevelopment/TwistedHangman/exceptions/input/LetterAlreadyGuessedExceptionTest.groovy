package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:02 PM
 */
class LetterAlreadyGuessedExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'Letter previously guessed.' == new LetterAlreadyGuessedException().message
    }
}
