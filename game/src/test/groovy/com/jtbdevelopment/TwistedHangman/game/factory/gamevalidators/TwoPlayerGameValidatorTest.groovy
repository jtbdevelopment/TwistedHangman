package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

import static org.junit.Assert.assertFalse

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class TwoPlayerGameValidatorTest extends TwistedHangmanTestCase {
    TwoPlayerGameValidator validator = new TwoPlayerGameValidator()


    @Test
    void testErrorMessage() {
        assert "Game's two player marker is wrong." == validator.errorMessage()
    }


    @Test
    void testTwoPlayersIsGood() {
        Game game = makeSimpleGame("1")
        game.features += GameFeature.TwoPlayer
        game.players = [PTWO, PTHREE]

        assert validator.validateGame(game)
    }


    @Test
    void testThreePlayersWithFlagIsGood() {
        Game game = makeSimpleGame("1")
        game.features += GameFeature.TwoPlayer
        game.players = [PONE, PFOUR, PTWO]

        assertFalse validator.validateGame(game)
    }


    @Test
    void testTwoPlayersWithoutFlagIsBad() {
        Game game = makeSimpleGame("1")
        game.players = [PONE, PFOUR]

        assertFalse validator.validateGame(game)
    }
}
