package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 6:50 AM
 */
class SinglePlayerExpanderTest extends GroovyTestCase {
    SinglePlayerExpander expander = new SinglePlayerExpander()

    @Test
    public void testIgnoresNonSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        Set<GameFeature> input = expected.toList() as Set
        expander.enhanceFeatureSet(input, ["1", "2"])
        assert expected == input
    }

    @Test
    public void testSetsForSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner]
        Set<GameFeature> input = [] as Set
        expander.enhanceFeatureSet(input, ["1"])
        assert expected == input
    }

    @Test
    public void testSetsForSinglePlayerWithConflictingOptions() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner, GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter]
        Set<GameFeature> input = [GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter] as Set
        expander.enhanceFeatureSet(input, ["1"])
        assert expected == input
    }
}
