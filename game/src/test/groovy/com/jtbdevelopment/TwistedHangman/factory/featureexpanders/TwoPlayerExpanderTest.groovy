package com.jtbdevelopment.TwistedHangman.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import org.junit.Test

/**
 * Date: 11/3/14
 * Time: 9:42 PM
 */
class TwoPlayerExpanderTest extends GroovyTestCase {
    TwoPlayerExpander expander = new TwoPlayerExpander()

    @Test
    public void testExpandsWhenTwoPlayersAndNoOtherChallengerOption() {
        Set<HangmanGameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2"])
        assert features.contains(HangmanGameFeature.TwoPlayersOnly)
    }

    @Test
    public void testDoesNotExpandsWhenTwoPlayersAndSystemChallengerOption() {
        Set<HangmanGameFeature> features = [HangmanGameFeature.SystemPuzzles] as Set
        expander.enhanceFeatureSet(features, ["1", "2"])
        assert !features.contains(HangmanGameFeature.TwoPlayersOnly)
    }

    @Test
    public void testDoesNotExpandsWhenTwoPlayersAndAlternatingChallengerOption() {
        Set<HangmanGameFeature> features = [HangmanGameFeature.AlternatingPuzzleSetter] as Set
        expander.enhanceFeatureSet(features, ["1", "2"])
        assert !features.contains(HangmanGameFeature.TwoPlayersOnly)
    }

    @Test
    public void testDoesNotExpandsWhenLessThanTwoPlayersAndNoOtherChallengerOption() {
        Set<HangmanGameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1"])
        assert !features.contains(HangmanGameFeature.TwoPlayersOnly)
    }

    @Test
    public void testDoesNotExpandsWhenMoreThanTwoPlayersAndNoOtherChallengerOption() {
        Set<HangmanGameFeature> features = [] as Set
        expander.enhanceFeatureSet(features, ["1", "2", "3"])
        assert !features.contains(HangmanGameFeature.TwoPlayersOnly)
    }
}
