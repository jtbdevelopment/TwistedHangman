package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:24 PM
 */
class PlayerScoreInitializerTest extends TwistedHangmanTestCase {
    PlayerScoreInitializer initializer = new PlayerScoreInitializer()

    @Test
    public void testInitializesScoresToZeroForAllPlayersIfNotSet() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE, PFOUR]
        game.players = players
        initializer.initializeGame(game)
        players.each {
            assert game.playerScores.containsKey(it)
            assert game.playerScores[it] == 0
        }
    }

    @Test
    public void testLeavesExistingScoresAlone() {
        Game game = new Game()
        Map<Player, Integer> players = [(PONE): 2, (PTWO): 0, (PTHREE): 1, (PFOUR): 0]
        game.players = players.keySet().toList()
        game.playerScores = [(PONE): 2, (PTHREE): 1]
        initializer.initializeGame(game)
        players.keySet().each {
            assert game.playerScores.containsKey(it)
            assert game.playerScores[it] == players[it]
        }
    }
}
