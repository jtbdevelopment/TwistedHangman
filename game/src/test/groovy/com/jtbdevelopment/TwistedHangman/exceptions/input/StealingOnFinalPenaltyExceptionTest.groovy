package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 6:45 PM
 */
class StealingOnFinalPenaltyExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new StealingOnFinalPenaltyException().message == "Can't steal a letter with only one penalty left."
    }
}
