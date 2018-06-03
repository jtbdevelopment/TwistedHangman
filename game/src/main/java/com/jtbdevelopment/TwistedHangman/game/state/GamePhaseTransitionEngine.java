package com.jtbdevelopment.TwistedHangman.game.state;

import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.scoring.GameScorer;
import com.jtbdevelopment.games.state.transition.AbstractMPGamePhaseTransitionEngine;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Date: 11/8/2014 Time: 4:18 PM
 */
@Component
public class GamePhaseTransitionEngine extends
    AbstractMPGamePhaseTransitionEngine<ObjectId, GameFeature, Game> {

  public GamePhaseTransitionEngine(final GameScorer<Game> gameScorer) {
    super(gameScorer);
  }

  @Override
  protected Game evaluateSetupPhase(final Game game) {
    if (game.getSolverStates().values().stream()
        .anyMatch(gameState -> StringUtils.isEmpty(gameState.getWordPhraseString()))) {
      return game;
    }
    return changeStateAndReevaluate(GamePhase.Playing, game);
  }

  @Override
  protected Game evaluatePlayingPhase(final Game game) {
    boolean oneSolved = game.getSolverStates().values().stream()
        .anyMatch(IndividualGameState::isPuzzleSolved);
    long pending = game.getSolverStates().values().stream()
        .filter(gameState -> !gameState.isPuzzleOver()).count();
    if (pending == 0 || (oneSolved && game.getFeatures().contains(GameFeature.SingleWinner))) {
      game.setCompletedTimestamp(Instant.now());
      return changeStateAndReevaluate(GamePhase.RoundOver, game);
    }
    return game;
  }


}
