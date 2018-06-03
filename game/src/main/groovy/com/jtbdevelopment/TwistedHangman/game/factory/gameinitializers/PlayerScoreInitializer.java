package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 10:00 PM
 */
@Component
public class PlayerScoreInitializer implements GameInitializer<Game> {

  @Override
  public void initializeGame(final Game game) {
    game.getPlayers().forEach(player -> {
      game.getPlayerRoundScores().put(player.getId(), 0);
      if (!game.getPlayerRunningScores().containsKey(player.getId())) {
        game.getPlayerRunningScores().put(player.getId(), 0);
      }
    });
  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }
}
