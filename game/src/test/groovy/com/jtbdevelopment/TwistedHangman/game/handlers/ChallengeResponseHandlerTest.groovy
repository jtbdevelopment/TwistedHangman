package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.TooLateToRespondToChallenge
import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/9/2014
 * Time: 8:21 PM
 */
class ChallengeResponseHandlerTest extends THGroovyTestCase {
    ChallengeResponseHandler handler = new ChallengeResponseHandler()

    @Test
    public void testExceptionOnBadPhases() {
        Game.GamePhase.values().findAll { it != Game.GamePhase.Declined && it != Game.GamePhase.Challenge }.each {
            Game game = new Game(gamePhase: it)
            try {
                handler.handleActionInternal(PONE, game, Game.PlayerChallengeState.Rejected)
                fail("Should have exceptioned on state " + it)
            } catch (TooLateToRespondToChallenge e) {
                //
            }
        }
    }

    @Test
    public void testSetsStateForPlayer() {
        [Game.GamePhase.Declined, Game.GamePhase.Challenge].each {
            Game.GamePhase gamePhase ->
                Game.PlayerChallengeState.findAll { it != Game.PlayerChallengeState.Pending }.each {
                    Game.PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE)  : Game.PlayerChallengeState.Pending,
                                               (PTWO)  : Game.PlayerChallengeState.Rejected,
                                               (PTHREE): Game.PlayerChallengeState.Pending,
                                               (PFOUR) : Game.PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE] == response
                        assert game.playerStates[PTWO] == Game.PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE] == Game.PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR] == Game.PlayerChallengeState.Accepted
                }
        }
    }

    @Test
    public void testOverridesResponseForPlayer() {
        [Game.GamePhase.Declined, Game.GamePhase.Challenge].each {
            Game.GamePhase gamePhase ->
                Game.PlayerChallengeState.findAll { it != Game.PlayerChallengeState.Pending }.each {
                    Game.PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE)  : Game.PlayerChallengeState.Accepted,
                                               (PTWO)  : Game.PlayerChallengeState.Rejected,
                                               (PTHREE): Game.PlayerChallengeState.Pending,
                                               (PFOUR) : Game.PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE] == response
                        assert game.playerStates[PTWO] == Game.PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE] == Game.PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR] == Game.PlayerChallengeState.Accepted
                }
        }
    }
}
