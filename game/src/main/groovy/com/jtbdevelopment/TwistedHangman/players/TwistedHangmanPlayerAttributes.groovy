package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import groovy.transform.CompileStatic

/**
 * Date: 1/30/15
 * Time: 6:34 PM
 */
@CompileStatic
class TwistedHangmanPlayerAttributes implements GameSpecificPlayerAttributes {
    public static final int DEFAULT_FREE_GAMES_PER_DAY = 10;

    int freeGamesUsedToday = 0
    int availablePurchasedGames = 0
}
