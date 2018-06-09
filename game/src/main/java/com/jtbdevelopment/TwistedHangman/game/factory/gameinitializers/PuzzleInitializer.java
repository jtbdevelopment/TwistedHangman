package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 7:03 AM
 */
@Component
public class PuzzleInitializer implements GameInitializer<THGame> {

  @Override
  public void initializeGame(final THGame game) {
    game.getPlayers()
        .stream()
        .filter(p -> p.getId() != game.getWordPhraseSetter())
        .forEach(player ->
            game.getSolverStates()
                .put(player.getId(), new IndividualGameState(game.getFeatures())));
  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }
}
