package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:13 PM
 */
class StealingPositionBeyondEndExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert "Can't steal letter after the end of the word/phrase." == new StealingPositionBeyondEndException().message
    }
}
