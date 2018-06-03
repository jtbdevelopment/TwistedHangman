package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/7/14
 * Time: 8:19 PM
 */
class GameFactoryTest extends TwistedHangmanTestCase {
    GameFactory gameFactory = new GameFactory(Collections.emptyList(), Collections.emptyList())

    @Test
    void testCreatingNewGame() {
        assert Game.class == gameFactory.newGame().class
    }


    @Test
    void testCreatingRematchGameCopiesRunningScores() {
        def expected = [(PONE.id): 5, (PTWO.id): 15]
        Game originalGame = new Game(playerRunningScores: expected)
        Game newGame = new Game()
        gameFactory.copyFromPreviousGame(originalGame, newGame)
        assert expected == newGame.playerRunningScores
    }


}
