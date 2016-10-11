package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.player.tracking.AbstractPlayerGameTrackingAttributes
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.players.PlayerPayLevel
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Transient

/**
 * Date: 1/30/15
 * Time: 6:34 PM
 */
@CompileStatic
class TwistedHangmanPlayerAttributes extends AbstractPlayerGameTrackingAttributes {
    public static final int DEFAULT_FREE_GAMES_PER_DAY = 1000;
    public static final int DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY = 5000;

    @Transient
    int maxDailyFreeGames

    @Transient
    @Override
    void setPlayer(final Player player) {
        super.setPlayer(player)
        maxDailyFreeGames = (player.payLevel == PlayerPayLevel.FreeToPlay ? DEFAULT_FREE_GAMES_PER_DAY : DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY)
    }
}
