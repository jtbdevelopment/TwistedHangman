package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:03 PM
 */
class NotALetterGuessExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'Guess is not a letter.' == new NotALetterGuessException().message
    }
}
