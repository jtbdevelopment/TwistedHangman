package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class SinglePlayerGameValidatorTest extends GroovyTestCase {
    SinglePlayerGameValidator validator = new SinglePlayerGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's single player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testSinglePlayersIsGood() {
        Game game = new Game()
        game.features += GameFeature.SinglePlayer
        game.players = ["1"]

        assert validator.validateGame(game)
    }

    @Test
    void testThreePlayersWithFlagIsGood() {
        Game game = new Game()
        game.features += GameFeature.SinglePlayer
        game.players = ["1", "2", "3"]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testSinglePlayerWithoutFlagIsBad() {
        Game game = new Game()
        game.players = ["1"]

        assertFalse validator.validateGame(game)
    }
}
