package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/6/14 Time: 6:48 AM
 */
@Component
public class SinglePlayerInitializer implements GameInitializer<Game> {

  public void initializeGame(final Game game) {
    if (game.getPlayers().size() == 1) {
      game.getFeatures().add(GameFeature.SinglePlayer);
      game.getFeatures().add(GameFeature.SingleWinner);
      game.getFeatures().add(GameFeature.SystemPuzzles);
    }

  }

  public int getOrder() {
    return EARLY_ORDER;
  }

}
