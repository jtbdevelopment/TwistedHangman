package com.jtbdevelopment.TwistedHangman.game.setup;

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;

/**
 * Date: 1/13/15 Time: 7:11 AM
 */
public interface PhraseFeatureInitializer {

  void initializeForPhrase(final IndividualGameState gameState);
}
