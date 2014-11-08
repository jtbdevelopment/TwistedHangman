package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:39 PM
 */
@CompileStatic
@Component
class MultiplePlayerExpander implements FeatureExpander {
    @Override
    void enhanceFeatureSet(final Set<GameFeature> features, final List<String> players) {
        if (players.size() > 2) {
            features.add(GameFeature.ThreePlus)
        }
    }
}