package com.jtbdevelopment.TwistedHangman.dictionary

import org.junit.BeforeClass
import org.junit.Test

/**
 * Date: 10/28/14
 * Time: 10:12 PM
 */
class ValidatorImplTest extends GroovyTestCase {
    private static ValidatorImpl validator;

    @BeforeClass
    public synchronized void setUp() {
        if (!validator) {
            validator = new ValidatorImpl();
            validator.dictionary = new AspellCaseInsensitiveDictionary()
        }
    }

    @Test
    public void testValidPhrase() {
        assertNull validator.validateWordPhrase("To be or not to be")
    }

    @Test
    public void testInvalidJunkWord() {
        assert validator.validateWordPhrase("To be or not to bestaffingle") == "bestaffingle"
    }

    @Test
    public void testInvalidOffensiveWord() {
        assert validator.validateWordPhrase("To be or not to wop") == "wop"
    }

    @Test
    public void testInvalidProfaneWord() {
        assert validator.validateWordPhrase("To cunts or not to be") == "cunts"
    }

    @Test
    public void testValidPhraseWithPeriod() {
        assertNull validator.validateWordPhrase("To be or not to be.")
    }

    @Test
    public void testValidPhraseWithQuestion() {
        assertNull validator.validateWordPhrase("To be or not to be?")
    }

    @Test
    public void testValidPhraseWithExclamation() {
        assertNull validator.validateWordPhrase("To be or not to be!")
    }

    @Test
    public void testValidPhraseWithRandom() {
        //  Valid because || will be shown
        assertNull validator.validateWordPhrase("To be || not to be!")
    }

    @Test
    public void testValidPhraseWithEndingPossesive() {
        assertNull validator.validateWordPhrase("That is Amadeus'")
    }

    @Test
    public void testMultiSentenceWordPhrase() {
        assertNull validator.validateWordPhrase("To be or not to be.  That is the question.")
    }

    @Test
    public void testInvalidEmptyPhrase() {
        assert validator.validateWordPhrase("") == ""
    }

    @Test
    public void testInvalidNullPhrase() {
        assert validator.validateWordPhrase(null) == ""
    }


    @Test
    public void testInvalidSpacePhrase() {
        assert validator.validateWordPhrase("  ") == "  "
    }

    @Test
    public void testInvalidPuncationOnlyPhrase() {
        assert validator.validateWordPhrase(" . ") == " . "
    }
}
