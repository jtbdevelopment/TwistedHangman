package com.jtbdevelopment.TwistedHangman.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:27 PM
 */
@CompileStatic
@Component
class ThievingExpander implements FeatureExpander {
    @Override
    void enhanceFeatureSet(final Set<HangmanGameFeature> features, final List<String> players) {
        if (features.contains(HangmanGameFeature.Thieving)) {
            features.add(HangmanGameFeature.ThievingPositionTracking)
            features.add(HangmanGameFeature.ThievingCountTracking)
        }
    }
}
