package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:04 PM
 */
@Component
public class ThievingPuzzleStateInitializer implements GameInitializer<Game> {

  public void initializeGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.Thieving)) {
      game.getSolverStates().values().forEach(gameState -> {
        gameState.getFeatureData().put(GameFeature.ThievingCountTracking, 0);
        gameState.getFeatureData()
            .put(GameFeature.ThievingPositionTracking, Collections.emptyList());
        gameState.getFeatureData().put(GameFeature.ThievingLetters, new ArrayList<>());
      });
    }

  }

  public int getOrder() {
    return LATE_ORDER;
  }
}
