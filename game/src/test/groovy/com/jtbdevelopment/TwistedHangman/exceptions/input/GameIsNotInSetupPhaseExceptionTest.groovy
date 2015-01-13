package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 6:56 PM
 */
class GameIsNotInSetupPhaseExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new GameIsNotInSetupPhaseException().message == "Can't set puzzle when game is not in setup phase."
    }
}
