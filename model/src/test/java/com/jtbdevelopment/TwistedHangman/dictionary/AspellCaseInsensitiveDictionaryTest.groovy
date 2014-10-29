package com.jtbdevelopment.TwistedHangman.dictionary

import org.junit.BeforeClass
import org.junit.Test

/**
 * Date: 10/27/14
 * Time: 7:01 PM
 */
class AspellCaseInsensitiveDictionaryTest extends GroovyTestCase {
    private static AspellCaseInsensitiveDictionary dictionary;


    @BeforeClass
    public synchronized void setUp() {

        if (!dictionary) {
            dictionary = new AspellCaseInsensitiveDictionary();
        }
    }

    @Test
    public void testLowercaseWord() {
        assert dictionary.isValidWord("apple")
    }

    @Test
    public void testUppercaseWord() {
        assert dictionary.isValidWord("APPLE")
    }

    @Test
    public void testMixedCaseWord() {
        assert dictionary.isValidWord("Apple")
    }


    @Test
    public void testInvalidWord() {
        assert !dictionary.isValidWord("AppleFudge")
    }

    @Test
    public void testOffensive1Exclusion() {
        assert !dictionary.isValidWord("niggering")
    }


    @Test
    public void testOffensive2Exclusion() {
        assert !dictionary.isValidWord("KRAUT'S")
    }

    @Test
    public void testProfane1Exclusion() {
        assert !dictionary.isValidWord("sHittY")
    }

    @Test
    public void testProfane3Exclusion() {
        assert !dictionary.isValidWord("cunt")
    }
}
