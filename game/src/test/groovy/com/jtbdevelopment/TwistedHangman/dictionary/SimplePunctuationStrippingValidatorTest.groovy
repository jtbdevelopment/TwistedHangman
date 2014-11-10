package com.jtbdevelopment.TwistedHangman.dictionary

import org.junit.BeforeClass
import org.junit.Test

/**
 * Date: 10/28/14
 * Time: 10:12 PM
 */
class SimplePunctuationStrippingValidatorTest extends GroovyTestCase {
    private static SimplePunctuationStrippingValidator validator;

    @BeforeClass
    public synchronized void setUp() {
        if (!validator) {
            validator = new SimplePunctuationStrippingValidator();
            validator.dictionary = new AspellUSEnglishCaseInsensitiveDictionary()
        }
    }

    @Test
    public void testValidPhrase() {
        assert validator.validateWordPhrase("To be or not to be").size() == 0
    }

    @Test
    public void testInvalidJunkWord() {
        assert validator.validateWordPhrase("To be or not to bestaffingle") == ["bestaffingle"]
    }

    @Test
    public void testInvalidOffensiveWord() {
        assert validator.validateWordPhrase("To be or not to wop") == ["wop"]
    }

    @Test
    public void testInvalidProfaneWord() {
        assert validator.validateWordPhrase("To cunts or not to be") == ["cunts"]
    }

    @Test
    public void testValidPhraseWithPeriod() {
        assert validator.validateWordPhrase("To be or not to be.").size() == 0
    }

    @Test
    public void testValidPhraseWithQuestion() {
        assert validator.validateWordPhrase("To be or not to be?").size() == 0
    }

    @Test
    public void testValidPhraseWithExclamation() {
        assert validator.validateWordPhrase("To be or not to be!").size() == 0
    }

    @Test
    public void testValidPhraseWithRandom() {
        //  Valid because || will be shown
        assert validator.validateWordPhrase("To be || not to be!").size() == 0
    }

    @Test
    public void testValidPhraseWithHyphen() {
        assert validator.validateWordPhrase("HOW-TO BREATHE FOR DUMMIES").size() == 0
    }

    @Test
    public void testValidPossessive() {
        assert validator.validateWordPhrase("GIRLS' NIGHT OUT").size() == 0
    }

    @Test
    public void testValidPhraseWithEndingPossesive() {
        assert validator.validateWordPhrase("That is Amadeus'").size() == 0
    }

    @Test
    public void testMultiSentenceWordPhrase() {
        assert validator.validateWordPhrase("To be or not to be.  That is the question.").size() == 0
    }

    @Test
    public void testExcessSpacingPhrase() {
        assert validator.validateWordPhrase("To  be  or  not   to  be.  That   is   the   question .  ").size() == 0
    }

    @Test
    public void testInvalidEmptyPhrase() {
        assert validator.validateWordPhrase("") == [""]
    }

    @Test
    public void testInvalidNullPhrase() {
        assert validator.validateWordPhrase(null) == [""]
    }


    @Test
    public void testInvalidSpacePhrase() {
        assert validator.validateWordPhrase("  ") == ["  "]
    }

    @Test
    public void testInvalidPuncationOnlyPhrase() {
        assert validator.validateWordPhrase(" . ") == [" . "]
    }
}
