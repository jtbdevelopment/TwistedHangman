package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game

/**
 * Date: 11/5/14
 * Time: 7:24 PM
 */
class PlayerScoreInitializerTest extends TwistedHangmanTestCase {
    PlayerScoreInitializer initializer = new PlayerScoreInitializer()


    public void testInitializesScoresToZeroForAllPlayersIfNotSet() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE, PFOUR]
        game.players = players
        initializer.initializeGame(game)
        players.each {
            assert game.playerScores.containsKey(it.id)
            assert game.playerScores[it.id] == 0
        }
    }


    public void testLeavesExistingScoresAlone() {
        Game game = new Game()
        Map<String, Integer> players = [(PONE.id): 2, (PTWO.id): 0, (PTHREE.id): 1, (PFOUR.id): 0]
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerScores = [(PONE.id): 2, (PTHREE.id): 1]
        initializer.initializeGame(game)
        players.keySet().each {
            assert game.playerScores.containsKey(it)
            assert game.playerScores[it] == players[it]
        }
    }
}
