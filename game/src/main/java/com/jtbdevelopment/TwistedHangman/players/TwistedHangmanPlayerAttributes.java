package com.jtbdevelopment.TwistedHangman.players;

import com.jtbdevelopment.games.player.tracking.AbstractPlayerGameTrackingAttributes;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.players.PlayerPayLevel;
import org.springframework.data.annotation.Transient;

/**
 * Date: 1/30/15 Time: 6:34 PM
 */
public class TwistedHangmanPlayerAttributes extends AbstractPlayerGameTrackingAttributes {

  private static final int DEFAULT_FREE_GAMES_PER_DAY = 1000;
  private static final int DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY = 5000;
  @Transient
  private int maxDailyFreeGames;

  @Transient
  @Override
  public void setPlayer(final Player player) {
    super.setPlayer(player);
    maxDailyFreeGames = (player.getPayLevel().equals(PlayerPayLevel.FreeToPlay)
        ? DEFAULT_FREE_GAMES_PER_DAY : DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY);
  }

  public int getMaxDailyFreeGames() {
    return maxDailyFreeGames;
  }

  public void setMaxDailyFreeGames(int maxDailyFreeGames) {
    this.maxDailyFreeGames = maxDailyFreeGames;
  }
}
