package com.jtbdevelopment.TwistedHangman.players

/**
 * Date: 2/2/15
 * Time: 5:58 PM
 */
class TwistedHangmanPlayerAttributesTest extends GroovyTestCase {
    TwistedHangmanPlayerAttributes attributes = new TwistedHangmanPlayerAttributes()

    void testInitializesToZero() {
        assert attributes.availablePurchasedGames == 0
        assert attributes.freeGamesUsedToday == 0
    }
}
