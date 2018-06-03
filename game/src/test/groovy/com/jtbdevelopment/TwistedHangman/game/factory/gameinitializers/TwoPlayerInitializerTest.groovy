package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer
import org.junit.Test

/**
 * Date: 11/3/14
 * Time: 9:42 PM
 */
class TwoPlayerInitializerTest extends TwistedHangmanTestCase {
    TwoPlayerInitializer initializer = new TwoPlayerInitializer()

    @Test
    void testExpandsWhenTwoPlayers() {
        Game game = new Game(players: [PONE, PTWO])
        initializer.initializeGame(game)
        assert [GameFeature.TwoPlayer] as Set == game.features
    }


    @Test
    void testDoesNotExpandsWhenLessThanTwoPlayers() {
        Game game = new Game(players: [PONE])
        initializer.initializeGame(game)
        assert [] as Set == game.features
    }


    @Test
    void testDoesNotExpandsWhenMoreThanTwoPlayers() {
        Game game = new Game(players: [PONE, PTWO, PTHREE])
        initializer.initializeGame(game)
        assert [] as Set == game.features
    }

    @Test
    void testOrder() {
        assert GameInitializer.EARLY_ORDER == initializer.order
    }
}
