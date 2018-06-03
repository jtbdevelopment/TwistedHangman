package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 8:11 PM
 */
class TurnInitializerTest extends TwistedHangmanTestCase {
    TurnInitializer initializer = new TurnInitializer()

    @Test
    void testOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }

    @Test
    void testInitializesTurnToFirstPlayer() {
        Game game = new Game()
        game.features += GameFeature.TurnBased
        game.players = [PFOUR, PONE, PTWO, PTHREE]
        initializer.initializeGame(game)

        assert game.featureData[GameFeature.TurnBased] == PFOUR.id
    }

    @Test
    void testInitializesTurnToSecondPlayerIfFirstPlayerAlsoPuzzleSetter() {
        Game game = new Game()
        game.features += GameFeature.TurnBased
        game.players = [PFOUR, PONE, PTWO, PTHREE]
        game.wordPhraseSetter = PFOUR.id
        initializer.initializeGame(game)

        assert game.featureData[GameFeature.TurnBased] == PONE.id
    }

    @Test
    void testNotSetWhenNotAFeature() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE]
        initializer.initializeGame(game)

        assert game.featureData[GameFeature.TurnBased] == null
    }
}
