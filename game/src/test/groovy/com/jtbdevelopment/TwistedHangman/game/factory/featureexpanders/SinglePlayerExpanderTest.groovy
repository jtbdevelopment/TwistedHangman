package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/6/14
 * Time: 6:50 AM
 */
class SinglePlayerExpanderTest extends TwistedHangmanTestCase {
    SinglePlayerExpander expander = new SinglePlayerExpander()


    public void testIgnoresNonSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        Set<GameFeature> input = expected.toList() as Set
        expander.enhanceFeatureSet(input, [PONE, PTWO] as LinkedHashSet)
        assert expected == input
    }


    public void testSetsForSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner]
        Set<GameFeature> input = [] as Set
        expander.enhanceFeatureSet(input, [PONE] as LinkedHashSet)
        assert expected == input
    }


    public void testSetsForSinglePlayerWithConflictingOptions() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner, GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter]
        Set<GameFeature> input = [GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter] as Set
        expander.enhanceFeatureSet(input, [PONE] as LinkedHashSet)
        assert expected == input
    }
}
