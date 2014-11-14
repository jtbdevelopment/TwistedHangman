package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState

/**
 * Date: 11/5/14
 * Time: 7:34 PM
 */
class PlayerStateInitializerTest extends TwistedHangmanTestCase {
    PlayerStateInitializer playerStateInitializer = new PlayerStateInitializer()


    public void testInitializesAllPlayersToPendingAcceptingInitiatingPlayer() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE, PFOUR]
        game.players = players
        game.initiatingPlayer = PTHREE
        playerStateInitializer.initializeGame(game)
        assert game.playerStates.size() == 4
        assert game.playerStates[(PTHREE.id)] == PlayerChallengeState.Accepted
        players.findAll { it != PTHREE }.each {
            assert game.playerStates[it.id] == PlayerChallengeState.Pending
        }
    }
}
