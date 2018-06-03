package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 6:54 AM
 */
@Component
public class ChallengerInitializer implements GameInitializer<Game> {

  @Override
  public void initializeGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.SystemPuzzles)) {
      game.setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId());
    } else if (game.getFeatures().contains(GameFeature.AlternatingPuzzleSetter)) {
      game.setWordPhraseSetter(game.getPlayers().get(0).getId());
    } else if (game.getFeatures().contains(GameFeature.TwoPlayer)) {
      game.setWordPhraseSetter(null);
    }

    if (game.getWordPhraseSetter() != null) {
      game.getSolverStates().remove(game.getWordPhraseSetter());
    }

  }

  @Override
  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
