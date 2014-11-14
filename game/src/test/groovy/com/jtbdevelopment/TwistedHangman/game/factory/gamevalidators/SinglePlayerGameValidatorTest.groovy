package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class SinglePlayerGameValidatorTest extends TwistedHangmanTestCase {
    SinglePlayerGameValidator validator = new SinglePlayerGameValidator()


    void testErrorMessage() {
        assert "Game's single player marker is wrong." == validator.errorMessage()
    }


    void testSinglePlayersIsGood() {
        Game game = new Game(id: "G")
        game.features += GameFeature.SinglePlayer
        game.players = [PONE]

        assert validator.validateGame(game)
    }


    void testThreePlayersWithFlagIsGood() {
        Game game = new Game(id: "G")
        game.features += GameFeature.SinglePlayer
        game.players = [PONE, PTWO, PTHREE]

        assertFalse validator.validateGame(game)
    }


    void testSinglePlayerWithoutFlagIsBad() {
        Game game = new Game(id: "G")
        game.players = [PONE]

        assertFalse validator.validateGame(game)
    }
}
