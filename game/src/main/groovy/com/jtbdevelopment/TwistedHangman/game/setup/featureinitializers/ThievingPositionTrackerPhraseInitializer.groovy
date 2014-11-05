package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseFeatureInitializer
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:44 PM
 */
@CompileStatic
@Component
class ThievingPositionTrackerPhraseInitializer implements PhraseFeatureInitializer {
    @Override
    void initializeForPhrase(final IndividualGameState gameState) {
        if (gameState.features.contains(GameFeature.Thieving)) {
            gameState.featureData[GameFeature.ThievingPositionTracking] =
                    (1..(gameState.wordPhrase.length)).collect() { int c -> false }.toArray()

        }
    }
}
