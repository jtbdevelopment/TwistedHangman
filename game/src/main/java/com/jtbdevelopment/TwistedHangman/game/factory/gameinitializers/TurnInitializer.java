package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 7:12 AM
 */
@Component
public class TurnInitializer implements GameInitializer<THGame> {

  @Override
  public void initializeGame(final THGame game) {
    if (game.getFeatures().contains(GameFeature.TurnBased)) {
      game.getFeatureData().put(GameFeature.TurnBased, game.getPlayers().get(0).getId());
      if (game.getFeatureData().get(GameFeature.TurnBased).equals(game.getWordPhraseSetter())) {
        game.getFeatureData().put(GameFeature.TurnBased, game.getPlayers().get(1).getId());
      }

    }

  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
