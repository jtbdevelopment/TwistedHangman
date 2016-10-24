package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/3/14
 * Time: 9:42 PM
 */
class TwoPlayerInitializerTest extends TwistedHangmanTestCase {
    TwoPlayerInitializer initializer = new TwoPlayerInitializer()

    public void testExpandsWhenTwoPlayers() {
        Game game = new Game(players: [PONE, PTWO])
        initializer.initializeGame(game)
        assert [GameFeature.TwoPlayer] as Set == game.features
    }


    public void testDoesNotExpandsWhenLessThanTwoPlayers() {
        Game game = new Game(players: [PONE])
        initializer.initializeGame(game)
        assert [] as Set == game.features
    }


    public void testDoesNotExpandsWhenMoreThanTwoPlayers() {
        Game game = new Game(players: [PONE, PTWO, PTHREE])
        initializer.initializeGame(game)
        assert [] as Set == game.features
    }

    public void testOrder() {
        assert GameInitializer.EARLY_ORDER == initializer.order
    }
}
