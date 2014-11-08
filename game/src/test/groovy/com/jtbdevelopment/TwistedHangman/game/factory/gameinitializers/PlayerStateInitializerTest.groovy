package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:34 PM
 */
class PlayerStateInitializerTest extends THGroovyTestCase {
    PlayerStateInitializer playerStateInitializer = new PlayerStateInitializer()

    @Test
    public void testInitializesAllPlayersToPendingAcceptingInitiatingPlayer() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE, PFOUR]
        game.players = players
        game.initiatingPlayer = PTHREE
        playerStateInitializer.initializeGame(game)
        assert game.playerStates.size() == 4
        assert game.playerStates[(PTHREE)] == Game.PlayerChallengeState.Accepted
        players.findAll { it != PTHREE }.each {
            assert game.playerStates[it] == Game.PlayerChallengeState.Pending
        }
    }
}
