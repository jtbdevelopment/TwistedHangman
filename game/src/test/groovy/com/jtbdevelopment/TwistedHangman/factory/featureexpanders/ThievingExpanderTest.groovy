package com.jtbdevelopment.TwistedHangman.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import org.junit.Test

/**
 * Date: 11/3/14
 * Time: 9:29 PM
 */
class ThievingExpanderTest extends GroovyTestCase {
    private ThievingExpander expander = new ThievingExpander()

    @Test
    public void testDoesNothingToNoneThievingOptions() {
        HangmanGameFeature.values().findAll() { !HangmanGameFeature.Thieving.equals(it) }.each {
            Set<HangmanGameFeature> set = [it] as Set
            Set<HangmanGameFeature> expected = [it] as Set
            expander.enhanceFeatureSet(set, null)
            assert expected == set
        }
    }

    @Test
    public void testExpandsThievingOptions() {
        Set<HangmanGameFeature> set = [HangmanGameFeature.Thieving] as Set
        Set<HangmanGameFeature> expected = [HangmanGameFeature.ThievingPositionTracking, HangmanGameFeature.Thieving, HangmanGameFeature.ThievingCountTracking] as Set
        expander.enhanceFeatureSet(set, null)
        assert expected == set
    }
}
