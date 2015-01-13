package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 6:49 PM
 */
class FailedToCreateValidGameExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new FailedToCreateValidGameException('Too bad.').message == 'System failed to create a valid game.  Too bad.'
    }
}
