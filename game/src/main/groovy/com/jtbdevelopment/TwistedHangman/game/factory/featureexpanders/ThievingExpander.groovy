package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
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
    void enhanceFeatureSet(final Set<GameFeature> features, final Collection<Player> players) {
        if (features.contains(GameFeature.Thieving)) {
            features.add(GameFeature.ThievingPositionTracking)
            features.add(GameFeature.ThievingCountTracking)
        }
    }
}
