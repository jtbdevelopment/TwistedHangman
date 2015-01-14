package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:12 PM
 */
class StealingKnownLetterExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert "Can't steal what you already know!" == new StealingKnownLetterException().message
    }
}
