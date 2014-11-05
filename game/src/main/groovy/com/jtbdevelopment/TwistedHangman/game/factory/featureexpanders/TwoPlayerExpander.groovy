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
class TwoPlayerExpander implements FeatureExpander {
    @Override
    void enhanceFeatureSet(final Set<GameFeature> features, final List<String> players) {
        if (players.size() == 2 &&
                !features.contains(GameFeature.SystemPuzzles) &&
                !features.contains(GameFeature.AlternatingPuzzleSetter)) {
            features.add(GameFeature.TwoPlayersOnly)
        }
    }
}
