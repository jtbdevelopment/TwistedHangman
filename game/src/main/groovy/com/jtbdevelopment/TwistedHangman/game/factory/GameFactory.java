package com.jtbdevelopment.TwistedHangman.game.factory;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.factory.GameValidator;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/3/14 Time: 9:24 PM
 */
@Component
public class GameFactory extends AbstractMultiPlayerGameFactory<ObjectId, GameFeature, Game> {

  protected GameFactory(
      final List<GameInitializer> gameInitializers,
      final List<GameValidator> gameValidators) {
    super(gameInitializers, gameValidators);
  }

  @Override
  protected void copyFromPreviousGame(final Game previousGame, final Game newGame) {
    super.copyFromPreviousGame(previousGame, newGame);
    newGame.getPlayerRunningScores().putAll(previousGame.getPlayerRunningScores());
  }

  @Override
  protected Game newGame() {
    return new Game();
  }

}
