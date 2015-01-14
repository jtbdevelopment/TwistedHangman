package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:12 PM
 */
class StealingNegativePositionExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert "Can't steal before beginning of word/phrase." == new StealingNegativePositionException().message
    }
}
