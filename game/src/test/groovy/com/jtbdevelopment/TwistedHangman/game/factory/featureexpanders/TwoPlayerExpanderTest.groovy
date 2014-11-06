package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/3/14
 * Time: 9:42 PM
 */
class TwoPlayerExpanderTest extends GroovyTestCase {
    TwoPlayerExpander expander = new TwoPlayerExpander()

    @Test
    public void testExpandsWhenTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2"])
        assert features.contains(GameFeature.TwoPlayer)
    }

    @Test
    public void testDoesNotExpandsWhenLessThanTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1"])
        assert !features.contains(GameFeature.TwoPlayer)
    }

    @Test
    public void testDoesNotExpandsWhenMoreThanTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2", "3"])
        assert !features.contains(GameFeature.TwoPlayer)
    }
}
