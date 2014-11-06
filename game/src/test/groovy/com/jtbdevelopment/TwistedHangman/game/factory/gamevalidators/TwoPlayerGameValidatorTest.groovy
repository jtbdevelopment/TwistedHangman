package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class TwoPlayerGameValidatorTest extends GroovyTestCase {
    TwoPlayerGameValidator validator = new TwoPlayerGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's two player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testTwoPlayersIsGood() {
        Game game = new Game()
        game.features += GameFeature.TwoPlayer
        game.players = ["1", "2"]

        assert validator.validateGame(game)
    }

    @Test
    void testThreePlayersWithFlagIsGood() {
        Game game = new Game()
        game.features += GameFeature.TwoPlayer
        game.players = ["1", "2", "3"]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testTwoPlayersWithoutFlagIsBad() {
        Game game = new Game()
        game.players = ["1", "2"]

        assertFalse validator.validateGame(game)
    }
}
