package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class TwoPlayerGameValidatorTest extends TwistedHangmanTestCase {
    TwoPlayerGameValidator validator = new TwoPlayerGameValidator()


    void testErrorMessage() {
        assert "Game's two player marker is wrong." == validator.errorMessage()
    }


    void testTwoPlayersIsGood() {
        Game game = new Game(id: "X")
        game.features += GameFeature.TwoPlayer
        game.players = [PTWO, PTHREE]

        assert validator.validateGame(game)
    }


    void testThreePlayersWithFlagIsGood() {
        Game game = new Game(id: "X")
        game.features += GameFeature.TwoPlayer
        game.players = [PONE, PFOUR, PTWO]

        assertFalse validator.validateGame(game)
    }


    void testTwoPlayersWithoutFlagIsBad() {
        Game game = new Game(id: "X")
        game.players = [PONE, PFOUR]

        assertFalse validator.validateGame(game)
    }
}
