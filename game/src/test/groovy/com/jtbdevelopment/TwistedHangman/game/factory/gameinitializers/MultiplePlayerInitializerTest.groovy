package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/6/14
 * Time: 7:00 AM
 */
class MultiplePlayerInitializerTest extends TwistedHangmanTestCase {
    MultiplePlayerInitializer initializer = new MultiplePlayerInitializer()


    public void testExpandsWhenThreePlayers() {
        def game = new Game(players: [PONE, PTWO, PTHREE])
        initializer.initializeGame(game)
        assert game.features.contains(GameFeature.ThreePlus)
    }


    public void testExpandsWhenFivePlayers() {
        def game = new Game(players: [PONE, PTWO, PTHREE, PFOUR, PFIVE])
        initializer.initializeGame(game)
        assert game.features.contains(GameFeature.ThreePlus)
    }


    public void testDoesNotExpandsWhenTwoPlayers() {
        def game = new Game(players: [PONE, PTWO])
        initializer.initializeGame(game)
        assertFalse game.features.contains(GameFeature.ThreePlus)
    }


    public void testDoesNotExpandsWhenSinglePlayer() {
        def game = new Game(players: [PONE])
        initializer.initializeGame(game)
        assertFalse game.features.contains(GameFeature.ThreePlus)
    }

    public void testOrder() {
        assert GameInitializer.EARLY_ORDER == initializer.order
    }
}
