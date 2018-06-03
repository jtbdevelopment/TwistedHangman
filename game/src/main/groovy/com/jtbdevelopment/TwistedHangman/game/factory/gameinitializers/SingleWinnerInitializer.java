package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 6:39 PM
 */
@Component
public class SingleWinnerInitializer implements GameInitializer<Game> {

  @Override
  public void initializeGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.SingleWinner)) {
      game.getFeatureData().put(GameFeature.SingleWinner, "");
    }

  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
