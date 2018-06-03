package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/3/14 Time: 9:39 PM
 */
@Component
public class MultiplePlayerInitializer implements GameInitializer<Game> {

  public void initializeGame(final Game game) {
    if (game.getPlayers().size() > 2) {
      game.getFeatures().add(GameFeature.ThreePlus);
    }

  }

  public int getOrder() {
    return EARLY_ORDER;
  }

}
