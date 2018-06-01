package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException;
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/10/14 Time: 9:16 PM
 */
@Component
public class GuessLetterHandler extends AbstractPlayerRotatingGameActionHandler<Character> {

  private final HangmanGameActions gameActions;

  public GuessLetterHandler(final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
      final GameTransitionEngine<Game> transitionEngine,
      final GamePublisher<Game, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, Game, MaskedGame> gameMasker,
      final HangmanGameActions gameActions) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
    this.gameActions = gameActions;
  }

  protected Game handleActionInternal(MongoPlayer player, Game game, Character param) {
    if (!game.getGamePhase().equals(GamePhase.Playing)) {
      throw new GameIsNotInPlayModeException();
    }

    IndividualGameState state = game.getSolverStates().get(player.getId());
    if (state != null) {
      gameActions.guessLetter(state, param);
    } else {
      throw new PlayerNotSolvingAPuzzleException();
    }

    return game;
  }
}
