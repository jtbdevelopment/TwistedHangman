package com.jtbdevelopment.TwistedHangman.exceptions.input

/**
 * Date: 1/13/15
 * Time: 7:00 PM
 */
class InvalidPuzzleWordsExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert new InvalidPuzzleWordsException(['bal', 'fud', 'carr']).message == 'Your puzzle has invalid words [bal, fud, carr].'
    }
}
