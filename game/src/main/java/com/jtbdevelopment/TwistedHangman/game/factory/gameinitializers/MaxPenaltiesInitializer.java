package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/5/14 Time: 6:59 PM
 */
@Component
public class MaxPenaltiesInitializer implements GameInitializer<Game> {

  public void initializeGame(final Game game) {
    final int penalties =
        IndividualGameState.BASE_PENALTIES + (game.getFeatures().contains(GameFeature.DrawFace)
            ? IndividualGameState.FACE_PENALTIES : 0) + (
            game.getFeatures().contains(GameFeature.DrawGallows)
                ? IndividualGameState.GALLOWS_PENALTIES : 0);
    game.getSolverStates().values().forEach(gameState -> gameState.setMaxPenalties(penalties));
  }

  public int getOrder() {
    return LATE_ORDER;
  }

}
