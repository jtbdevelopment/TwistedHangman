package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:34 PM
 */
class PlayerStateInitializerTest extends GroovyTestCase {
    PlayerStateInitializer playerStateInitializer = new PlayerStateInitializer()

    @Test
    public void testInitializesAllPlayersToPendingAcceptingInitiatingPlayer() {
        Game game = new Game()
        def players = ["1", "2", "3", "4"]
        game.players = players
        game.initiatingPlayer = "3"
        playerStateInitializer.initializeGame(game)
        assert game.playerStates.size() == 4
        assert game.playerStates["3"] == Game.PlayerChallengeState.Accepted
        players.findAll { it != "3" }.each {
            assert game.playerStates[(it)] == Game.PlayerChallengeState.Pending
        }
    }
}
