package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers;

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseFeatureInitializer;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:44 PM
 */
@Component
public class ThievingPositionTrackerPhraseInitializer implements PhraseFeatureInitializer {

  @Override
  public void initializeForPhrase(final IndividualGameState gameState) {
    if (gameState.getFeatures().contains(GameFeature.Thieving)) {
      List<Boolean> thieving = new ArrayList<>();
      for (int i = 0; i < gameState.getWordPhrase().length; ++i) {
        thieving.add(false);
      }
      gameState.getFeatureData().put(GameFeature.ThievingPositionTracking, thieving);
    }
  }
}
