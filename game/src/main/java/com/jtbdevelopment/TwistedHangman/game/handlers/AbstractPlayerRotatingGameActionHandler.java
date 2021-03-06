package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.Optional;
import org.bson.types.ObjectId;

/**
 * Date: 11/9/2014 Time: 8:36 PM
 */
public abstract class AbstractPlayerRotatingGameActionHandler<T> extends
    AbstractGameActionHandler<T, ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> {

  AbstractPlayerRotatingGameActionHandler(
      final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, THGame> gameRepository,
      final GameTransitionEngine<THGame> transitionEngine,
      final GamePublisher<THGame, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, THGame, THMaskedGame> gameMasker) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
  }

  @Override
  protected THGame rotateTurnBasedGame(
      final THGame game) {
    if (game.getGamePhase() == GamePhase.Playing && game.getFeatures()
        .contains(GameFeature.TurnBased)) {
      rotateOnePlayer(game);

      if (game.getFeatureData().get(GameFeature.TurnBased) == game.getWordPhraseSetter()) {
        rotateOnePlayer(game);
      }
    }
    return game;
  }

  private void rotateOnePlayer(final THGame game) {
    Optional<Player<ObjectId>> first = game.getPlayers()
        .stream()
        .filter(p -> p.getId() == game.getFeatureData().get(GameFeature.TurnBased))
        .findFirst();
    first.ifPresent(player -> {
      int index = game.getPlayers().indexOf(player) + 1;
      if (index >= game.getPlayers().size()) {
        index = 0;
      }
      game.getFeatureData().put(GameFeature.TurnBased, game.getPlayers().get(index).getId());
    });
  }

}
