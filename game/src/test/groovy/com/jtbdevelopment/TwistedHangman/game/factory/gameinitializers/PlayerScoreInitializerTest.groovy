package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:24 PM
 */
class PlayerScoreInitializerTest extends GroovyTestCase {
    PlayerScoreInitializer initializer = new PlayerScoreInitializer()

    @Test
    public void testInitializesScoresToZeroForAllPlayersIfNotSet() {
        Game game = new Game()
        def players = ["1", "2", "3", "4"]
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
        def players = ["1": 2, "2": 0, "3": 1, "4": 0]
        game.players = players.keySet().toList()
        game.playerScores = ["1": 2, "3": 1]
        initializer.initializeGame(game)
        players.keySet().each {
            assert game.playerScores.containsKey(it)
            assert game.playerScores[it] == players[it]
        }
    }
}
