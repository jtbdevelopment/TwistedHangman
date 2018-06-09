package com.jtbdevelopment.TwistedHangman.game.mechanics;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.Set;

/**
 * Date: 10/25/2014 Time: 6:28 PM
 */
public abstract class AbstractGameActionsTest {

  private Object[] makeWorkingPhraseFromPhrase(final String phrase) {
    char[] working = new char[phrase.length()];
    int blanks = 0;
    for (int i = 0; i < phrase.length(); i = ++i) {
      if (Character.isLetter(phrase.charAt(i))) {
        working[i] = '_';
        blanks = ++blanks;
      } else {
        working[i] = phrase.charAt(i);
      }

    }

    return new Object[]{working, blanks};
  }

  protected IndividualGameState makeGameState(String wordPhrase, String category,
      int maxPenalties) {
    IndividualGameState gameState = new IndividualGameState(getGameFeatures());
    gameState.setWordPhrase(wordPhrase.toUpperCase().toCharArray());
    Object[] phraseAndCount = makeWorkingPhraseFromPhrase(wordPhrase);
    gameState.setWorkingWordPhrase((char[]) phraseAndCount[0]);
    gameState.setBlanksRemaining(((int) phraseAndCount[1]));
    gameState.setCategory(category);
    gameState.setMaxPenalties(maxPenalties);
    return gameState;
  }

  protected abstract Set<GameFeature> getGameFeatures();
}
