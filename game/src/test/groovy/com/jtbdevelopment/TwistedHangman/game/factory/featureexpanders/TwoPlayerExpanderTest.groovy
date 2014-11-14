package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/3/14
 * Time: 9:42 PM
 */
class TwoPlayerExpanderTest extends TwistedHangmanTestCase {
    TwoPlayerExpander expander = new TwoPlayerExpander()


    public void testExpandsWhenTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE, PTWO] as LinkedHashSet)
        assert features.contains(GameFeature.TwoPlayer)
    }


    public void testDoesNotExpandsWhenLessThanTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE] as LinkedHashSet)
        assert !features.contains(GameFeature.TwoPlayer)
    }


    public void testDoesNotExpandsWhenMoreThanTwoPlayers() {
        Set<GameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, [PONE, PTWO, PTHREE] as LinkedHashSet)
        assert !features.contains(GameFeature.TwoPlayer)
    }
}
