package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 10:12 PM
 */
class SinglePlayerGameValidatorTest extends THGroovyTestCase {
    SinglePlayerGameValidator validator = new SinglePlayerGameValidator()

    @Test
    void testErrorMessage() {
        assert "Game's single player marker is wrong." == validator.errorMessage()
    }

    @Test
    void testSinglePlayersIsGood() {
        Game game = new Game(id: "G")
        game.features += GameFeature.SinglePlayer
        game.players = [PONE]

        assert validator.validateGame(game)
    }

    @Test
    void testThreePlayersWithFlagIsGood() {
        Game game = new Game(id: "G")
        game.features += GameFeature.SinglePlayer
        game.players = [PONE, PTWO, PTHREE]

        assertFalse validator.validateGame(game)
    }

    @Test
    void testSinglePlayerWithoutFlagIsBad() {
        Game game = new Game(id: "G")
        game.players = [PONE]

        assertFalse validator.validateGame(game)
    }
}
