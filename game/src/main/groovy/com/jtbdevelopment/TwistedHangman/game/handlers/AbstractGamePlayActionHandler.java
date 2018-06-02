package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.exceptions.input.PlayerOutOfTurnException;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import org.bson.types.ObjectId;

/**
 * Date: 11/28/14 Time: 2:48 PM
 */
public abstract class AbstractGamePlayActionHandler<T> extends
    AbstractPlayerRotatingGameActionHandler<T> {

  public AbstractGamePlayActionHandler(
      final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
      final GameTransitionEngine<Game> transitionEngine,
      final GamePublisher<Game, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, Game, MaskedGame> gameMasker) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
  }

  @Override
  protected void validatePlayerForGame(final com.jtbdevelopment.games.state.Game game,
      final Player player) {
    super.validatePlayerForGame(game, player);
    if (game.getFeatures().contains(GameFeature.TurnBased)) {
      if (!game.getFeatureData().get(GameFeature.TurnBased).equals(player.getId())) {
        throw new PlayerOutOfTurnException();
      }
    }
  }

}
