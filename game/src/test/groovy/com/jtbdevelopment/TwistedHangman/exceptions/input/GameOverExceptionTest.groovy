package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 6:55 PM
 */
class GameOverExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new GameOverException().message == 'Game is already over.'
    }
}
