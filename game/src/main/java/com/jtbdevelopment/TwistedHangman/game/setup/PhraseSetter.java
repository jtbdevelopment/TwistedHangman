package com.jtbdevelopment.TwistedHangman.game.setup;

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:37 PM
 */
@Component
public class PhraseSetter {

  private final List<PhraseFeatureInitializer> phraseFeatureInitializers;

  public PhraseSetter(
      final List<PhraseFeatureInitializer> phraseFeatureInitializers) {
    this.phraseFeatureInitializers = phraseFeatureInitializers;
  }

  public void setWordPhrase(
      final IndividualGameState gameState,
      final String wordPhrase,
      final String category) {
    gameState.setCategory(category.toUpperCase());
    gameState.setWordPhrase(wordPhrase.toUpperCase().toCharArray());
    phraseFeatureInitializers.forEach(initializer -> initializer.initializeForPhrase(gameState));
  }
}
