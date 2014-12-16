package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/6/14
 * Time: 7:07 AM
 */
class ThreePlusGameValidatorTest extends TwistedHangmanTestCase {
    ThreePlusGameValidator validator = new ThreePlusGameValidator()


    void testErrorMessage() {
        assert "Game's 3+ player marker is wrong." == validator.errorMessage()
    }


    void testThreePlayersIsGood() {
        Game game = makeSimpleGame("1")
        game.features += GameFeature.ThreePlus
        game.players = [PONE, PTWO, PTHREE]

        assert validator.validateGame(game)
    }


    void testTwoPlayersWithFlagIsBad() {
        Game game = makeSimpleGame("1")
        game.features += GameFeature.ThreePlus
        game.players = [PTWO, PFOUR]

        assertFalse validator.validateGame(game)
    }


    void testFourPlayersWithoutFlagIsBad() {
        Game game = makeSimpleGame("1")
        game.players = [PONE, PTHREE, PFOUR, PTWO]

        assertFalse validator.validateGame(game)
    }
}
