package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/3/14
 * Time: 9:29 PM
 */
class ThievingExpanderTest extends GroovyTestCase {
    private ThievingExpander expander = new ThievingExpander()


    public void testDoesNothingToNoneThievingOptions() {
        GameFeature.values().findAll() { !GameFeature.Thieving.equals(it) }.each {
            Set<GameFeature> set = [it] as Set
            Set<GameFeature> expected = [it] as Set
            expander.enhanceFeatureSet(set, null)
            assert expected == set
        }
    }


    public void testExpandsThievingOptions() {
        Set<GameFeature> set = [GameFeature.Thieving] as Set
        Set<GameFeature> expected = [GameFeature.ThievingPositionTracking, GameFeature.Thieving, GameFeature.ThievingCountTracking] as Set
        expander.enhanceFeatureSet(set, null)
        assert expected == set
    }
}
