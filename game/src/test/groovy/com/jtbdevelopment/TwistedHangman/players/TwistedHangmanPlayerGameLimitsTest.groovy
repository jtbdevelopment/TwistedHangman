package com.jtbdevelopment.TwistedHangman.players

/**
 * Date: 4/9/15
 * Time: 9:50 AM
 */
class TwistedHangmanPlayerGameLimitsTest extends GroovyTestCase {
    TwistedHangmanPlayerGameLimits limits = new TwistedHangmanPlayerGameLimits()

    void testDefaultFree() {
        assert limits.defaultDailyFreeGames == TwistedHangmanPlayerGameLimits.DEFAULT_FREE_GAMES_PER_DAY
    }

    void testDefaultPremium() {
        assert limits.defaultDailyPremiumFreeGames == TwistedHangmanPlayerGameLimits.DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY
    }
}
