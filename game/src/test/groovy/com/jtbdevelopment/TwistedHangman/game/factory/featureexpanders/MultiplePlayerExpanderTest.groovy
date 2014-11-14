package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/6/14
 * Time: 7:00 AM
 */
class MultiplePlayerExpanderTest extends TwistedHangmanTestCase {
    MultiplePlayerExpander expander = new MultiplePlayerExpander()


    public void testExpandsWhenThreePlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE, PTWO, PTHREE] as LinkedHashSet)
        assert features.contains(GameFeature.ThreePlus)
    }


    public void testExpandsWhenFivePlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE, PTWO, PTHREE, PFOUR, PFIVE] as LinkedHashSet)
        assert features.contains(GameFeature.ThreePlus)
    }


    public void testDoesNotExpandsWhenTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE, PTWO] as LinkedHashSet)
        assert !features.contains(GameFeature.ThreePlus)
    }


    public void testDoesNotExpandsWhenSinglePlayer() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE] as LinkedHashSet)
        assert !features.contains(GameFeature.ThreePlus)
    }
}
