package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.factory.FeatureExpander
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/6/14
 * Time: 6:48 AM
 */
@CompileStatic
@Component
class SinglePlayerExpander implements FeatureExpander {

    @Override
    void enhanceFeatureSet(final Set<GameFeature> features, final List<String> players) {
        if (players.size() == 1) {
            features.add(GameFeature.SinglePlayer)
            features.add(GameFeature.SingleWinner)
            features.add(GameFeature.SystemPuzzles)
        }
    }
}
