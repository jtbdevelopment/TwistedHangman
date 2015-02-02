package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes

/**
 * Date: 2/2/15
 * Time: 5:59 PM
 */
class TwistedHangmanPlayerAttributesFactoryTest extends GroovyTestCase {
    TwistedHangmanPlayerAttributesFactory factory = new TwistedHangmanPlayerAttributesFactory()

    void testNewPlayer() {
        GameSpecificPlayerAttributes attributes = factory.newPlayerAttributes()
        assertNotNull attributes
        assert attributes instanceof TwistedHangmanPlayerAttributes
    }

    void testNewManualPlayer() {
        GameSpecificPlayerAttributes attributes = factory.newManualPlayerAttributes()
        assertNotNull attributes
        assert attributes instanceof TwistedHangmanPlayerAttributes
    }

    void testNewSystemPlayer() {
        assertNull factory.newSystemPlayerAttributes()
    }
}
