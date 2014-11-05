package com.jtbdevelopment.TwistedHangman.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
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
    void enhanceFeatureSet(final Set<GameFeature> features, final List<String> players) {
        if (features.contains(GameFeature.Thieving)) {
            features.add(GameFeature.ThievingPositionTracking)
            features.add(GameFeature.ThievingCountTracking)
        }
    }
}
