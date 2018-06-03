package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers;

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseFeatureInitializer;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:41 PM
 */
@Component
public class WorkingWordPhraseInitializer implements PhraseFeatureInitializer {

  @Override
  public void initializeForPhrase(final IndividualGameState gameState) {
    int length = gameState.getWordPhrase().length;
    char[] workingWordPhrase = new char[length];

    int blanks = 0;
    for (int i = 0; i < length; i = ++i) {
      if (Character.isLetter(gameState.getWordPhrase()[i])) {
        workingWordPhrase[i] = '_';
        blanks = ++blanks;
      } else {
        workingWordPhrase[i] = gameState.getWordPhrase()[i];
      }

    }

    gameState.setWorkingWordPhrase(workingWordPhrase);
    gameState.setBlanksRemaining(blanks);
  }

}
