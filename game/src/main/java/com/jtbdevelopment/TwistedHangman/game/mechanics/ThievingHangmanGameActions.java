package com.jtbdevelopment.TwistedHangman.game.mechanics;

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameOverException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingKnownLetterException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingNegativePositionException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingOnFinalPenaltyException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingPositionBeyondEndException;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Date: 10/25/2014 Time: 7:44 PM
 */
@Component
public class ThievingHangmanGameActions {

  private static void validateSteal(
      final IndividualGameState gameState,
      final int position,
      final List<Boolean> markers) {
    if (gameState.isPuzzleOver()) {
      throw new GameOverException();
    }

    if (position >= gameState.getWordPhrase().length) {
      throw new StealingPositionBeyondEndException();
    }

    if (position < 0) {
      throw new StealingNegativePositionException();
    }

    if (markers.get(position) ||
        gameState.getWordPhrase()[position] == gameState.getWorkingWordPhrase()[position]) {
      throw new StealingKnownLetterException();
    }

    if (gameState.getPenaltiesRemaining() == 1) {
      throw new StealingOnFinalPenaltyException();
    }

  }

  public void stealLetter(final IndividualGameState gameState, int position) {
    List<Boolean> markers = gameState.getFeatureData(GameFeature.ThievingPositionTracking);
    validateSteal(gameState, position, markers);

    Integer counter = gameState.getFeatureData(GameFeature.ThievingCountTracking);
    counter += 1;
    char c = gameState.getWordPhrase()[position];
    ((List<Character>) gameState.getFeatureData(GameFeature.ThievingLetters)).add(c);
    int found = 0;
    for (int i = 0; i < gameState.getWorkingWordPhrase().length; i++) {
      if (gameState.getWordPhrase()[i] == c) {
        gameState.getWorkingWordPhrase()[i] = c;
        markers.set(i, true);
        found = ++found;
      }

    }

    gameState.getFeatureData()
        .put(GameFeature.ThievingCountTracking, counter);
    gameState.getFeatureData()
        .put(GameFeature.ThievingPositionTracking, markers);
    gameState.setMoveCount(gameState.getMoveCount() + 1);
    gameState.setPenalties(gameState.getPenalties() + 1);
    gameState.setBlanksRemaining(gameState.getBlanksRemaining() - found);

    gameState.setWorkingWordPhraseString(new String(gameState.getWorkingWordPhrase()));
  }

}
