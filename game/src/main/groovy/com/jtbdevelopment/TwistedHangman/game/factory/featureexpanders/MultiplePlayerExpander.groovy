package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
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
    void enhanceFeatureSet(final Set<GameFeature> features, final LinkedHashSet<Player> players) {
        if (players.size() > 2) {
            features.add(GameFeature.ThreePlus)
        }
    }
}
