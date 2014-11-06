package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 7:00 AM
 */
class MultiplePlayerExpanderTest extends GroovyTestCase {
    MultiplePlayerExpander expander = new MultiplePlayerExpander()

    @Test
    public void testExpandsWhenThreePlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2", "3"])
        assert features.contains(GameFeature.ThreePlus)
    }

    @Test
    public void testExpandsWhenFivePlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2", "3", "4", "5"])
        assert features.contains(GameFeature.ThreePlus)
    }

    @Test
    public void testDoesNotExpandsWhenTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2"])
        assert !features.contains(GameFeature.ThreePlus)
    }

    @Test
    public void testDoesNotExpandsWhenSinglePlayer() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1"])
        assert !features.contains(GameFeature.ThreePlus)
    }
}
