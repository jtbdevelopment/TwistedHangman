package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.factory.GameInitializer
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:24 PM
 */
class PlayerScoreInitializerTest extends TwistedHangmanTestCase {
    PlayerScoreInitializer initializer = new PlayerScoreInitializer()

    @Test
    public void testOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }


    @Test
    public void testInitializesScoresToZeroForAllPlayersIfNotSet() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE, PFOUR]
        game.players = players
        initializer.initializeGame(game)
        players.each {
            assert game.playerRunningScores.containsKey(it.id)
            assert game.playerRunningScores[it.id] == 0
            assert game.playerRoundScores.containsKey(it.id)
            assert game.playerRoundScores[it.id] == 0
        }
    }


    @Test
    public void testLeavesExistingScoresAlone() {
        Game game = new Game()
        Map<String, Integer> players = [(PONE.id): 2, (PTWO.id): 0, (PTHREE.id): 1, (PFOUR.id): 0]
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerRunningScores = [(PONE.id): 2, (PTHREE.id): 1]
        initializer.initializeGame(game)
        players.keySet().each {
            assert game.playerRunningScores.containsKey(it)
            assert game.playerRunningScores[it] == players[it]
            assert game.playerRoundScores.containsKey(it)
            assert game.playerRoundScores[it] == 0
        }
    }
}
