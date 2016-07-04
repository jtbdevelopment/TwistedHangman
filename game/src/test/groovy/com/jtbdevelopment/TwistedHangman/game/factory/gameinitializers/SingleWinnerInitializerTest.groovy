package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/5/14
 * Time: 8:08 PM
 */
class SingleWinnerInitializerTest extends GroovyTestCase {
    SingleWinnerInitializer initializer = new SingleWinnerInitializer()

    public void testOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }

    public void testInitializesWinner() {
        Game game = new Game()
        game.features += GameFeature.SingleWinner

        initializer.initializeGame(game)

        assert game.featureData[GameFeature.SingleWinner] == ""
    }


    public void testOverwritesWinner() {
        Game game = new Game()
        game.features += GameFeature.SingleWinner
        game.featureData[GameFeature.SingleWinner] = 12

        initializer.initializeGame(game)

        assert game.featureData[GameFeature.SingleWinner] == ""
    }


    public void testLeavesUnsetIfNotInFeatures() {
        Game game = new Game()

        initializer.initializeGame(game)

        assertFalse game.featureData.containsKey(GameFeature.SingleWinner)
    }
}
