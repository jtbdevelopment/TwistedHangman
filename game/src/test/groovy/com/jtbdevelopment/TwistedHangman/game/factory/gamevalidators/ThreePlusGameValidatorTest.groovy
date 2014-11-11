package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 7:07 AM
 */
class ThreePlusGameValidatorTest extends TwistedHangmanTestCase {
    ThreePlusGameValidator validator = new ThreePlusGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's 3+ player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testThreePlayersIsGood() {
        Game game = new Game(id: "Y")
        game.features += GameFeature.ThreePlus
        game.players = [PONE, PTWO, PTHREE]

        assert validator.validateGame(game)
    }

    @Test
    void testTwoPlayersWithFlagIsBad() {
        Game game = new Game(id: "Y")
        game.features += GameFeature.ThreePlus
        game.players = [PTWO, PFOUR]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testFourPlayersWithoutFlagIsBad() {
        Game game = new Game(id: "Y")
        game.players = [PONE, PTHREE, PFOUR, PTWO]

        assertFalse validator.validateGame(game)
    }
}
