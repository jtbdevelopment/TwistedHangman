package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 6:39 PM
 */
@Component
public class SingleWinnerInitializer implements GameInitializer<THGame> {

  @Override
  public void initializeGame(final THGame game) {
    if (game.getFeatures().contains(GameFeature.SingleWinner)) {
      game.getFeatureData().put(GameFeature.SingleWinner, "");
    }

  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
