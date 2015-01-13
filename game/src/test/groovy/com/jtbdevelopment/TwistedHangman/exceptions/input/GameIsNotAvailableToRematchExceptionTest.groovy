package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 6:51 PM
 */
class GameIsNotAvailableToRematchExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new GameIsNotAvailableToRematchException().message == 'Game is not available for rematching.'
    }
}
