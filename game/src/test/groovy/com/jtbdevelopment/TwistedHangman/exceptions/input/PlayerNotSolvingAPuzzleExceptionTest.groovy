package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:08 PM
 */
class PlayerNotSolvingAPuzzleExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'Player is not one of the solvers in this game.' == new PlayerNotSolvingAPuzzleException().message
    }
}
