package com.jtbdevelopment.TwistedHangman.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:39 PM
 */
@CompileStatic
@Component
class TwoPlayerExpander implements FeatureExpander {
    @Override
    void enhanceFeatureSet(final Set<HangmanGameFeature> features, final List<String> players) {
        if (players.size() == 2 &&
                !features.contains(HangmanGameFeature.SystemPuzzles) &&
                !features.contains(HangmanGameFeature.AlternatingPuzzleSetter)) {
            features.add(HangmanGameFeature.TwoPlayersOnly)
        }
    }
}
