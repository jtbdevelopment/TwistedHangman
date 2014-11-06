package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 7:07 AM
 */
class ThreePlusGameValidatorTest extends GroovyTestCase {
    ThreePlusGameValidator validator = new ThreePlusGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's 3+ player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testThreePlayersIsGood() {
        Game game = new Game()
        game.features += GameFeature.ThreePlus
        game.players = ["1", "2", "3"]

        assert validator.validateGame(game)
    }

    @Test
    void testTwoPlayersWithFlagIsBad() {
        Game game = new Game()
        game.features += GameFeature.ThreePlus
        game.players = ["1", "2"]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testFourPlayersWithoutFlagIsBad() {
        Game game = new Game()
        game.players = ["1", "2", "3", "4"]

        assertFalse validator.validateGame(game)
    }
}
