package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/6/14
 * Time: 6:50 AM
 */
class SinglePlayerInitializerTest extends TwistedHangmanTestCase {
    SinglePlayerInitializer initializer = new SinglePlayerInitializer()


    public void testIgnoresNonSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        Game game = new Game(players: [PONE, PTWO], features: expected)
        initializer.initializeGame(game)
        assert expected == game.features
    }


    public void testSetsForSinglePlayer() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner]
        Game game = new Game(players: [PONE])
        initializer.initializeGame(game)
        assert expected == game.features
    }


    public void testSetsForSinglePlayerWithConflictingOptions() {
        Set<GameFeature> expected = [GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner, GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter]
        Game game = new Game(players: [PONE], features: [GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter] as Set)
        initializer.initializeGame(game)
        assert expected == game.features
    }

    public void testOrder() {
        assert GameInitializer.EARLY_ORDER == initializer.order
    }
}
