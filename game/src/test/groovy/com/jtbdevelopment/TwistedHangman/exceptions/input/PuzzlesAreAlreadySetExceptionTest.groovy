package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:10 PM
 */
class PuzzlesAreAlreadySetExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'You have already set your puzzles.' == new PuzzlesAreAlreadySetException().message
    }
}
