package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/3/14 Time: 9:27 PM
 */
@Component
public class ThievingInitializer implements GameInitializer<THGame> {

  public void initializeGame(final THGame game) {
    if (game.getFeatures().contains(GameFeature.Thieving)) {
      game.getFeatures().add(GameFeature.ThievingPositionTracking);
      game.getFeatures().add(GameFeature.ThievingCountTracking);
      game.getFeatures().add(GameFeature.ThievingLetters);
    }

  }

  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
