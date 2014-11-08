package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class TwoPlayerGameValidatorTest extends THGroovyTestCase {
    TwoPlayerGameValidator validator = new TwoPlayerGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's two player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testTwoPlayersIsGood() {
        Game game = new Game(id: "X")
        game.features += GameFeature.TwoPlayer
        game.players = [PTWO, PTHREE]

        assert validator.validateGame(game)
    }

    @Test
    void testThreePlayersWithFlagIsGood() {
        Game game = new Game(id: "X")
        game.features += GameFeature.TwoPlayer
        game.players = [PONE, PFOUR, PTWO]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testTwoPlayersWithoutFlagIsBad() {
        Game game = new Game(id: "X")
        game.players = [PONE, PFOUR]

        assertFalse validator.validateGame(game)
    }
}
