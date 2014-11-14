package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.TooLateToRespondToChallenge
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState

/**
 * Date: 11/9/2014
 * Time: 8:21 PM
 */
class ChallengeResponseHandlerTest extends TwistedHangmanTestCase {
    ChallengeResponseHandler handler = new ChallengeResponseHandler()


    public void testExceptionOnBadPhases() {
        GamePhase.values().findAll { it != GamePhase.Declined && it != GamePhase.Challenge }.each {
            Game game = new Game(gamePhase: it)
            try {
                handler.handleActionInternal(PONE, game, PlayerChallengeState.Rejected)
                fail("Should have exceptioned on state " + it)
            } catch (TooLateToRespondToChallenge e) {
                //
            }
        }
    }


    public void testSetsStateForPlayer() {
        [GamePhase.Declined, GamePhase.Challenge].each {
            GamePhase gamePhase ->
                PlayerChallengeState.findAll { it != PlayerChallengeState.Pending }.each {
                    PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : PlayerChallengeState.Pending,
                                               (PTWO.id)  : PlayerChallengeState.Rejected,
                                               (PTHREE.id): PlayerChallengeState.Pending,
                                               (PFOUR.id) : PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE.id] == PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR.id] == PlayerChallengeState.Accepted
                }
        }
    }


    public void testOverridesResponseForPlayer() {
        [GamePhase.Declined, GamePhase.Challenge].each {
            GamePhase gamePhase ->
                PlayerChallengeState.findAll { it != PlayerChallengeState.Pending }.each {
                    PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : PlayerChallengeState.Accepted,
                                               (PTWO.id)  : PlayerChallengeState.Rejected,
                                               (PTHREE.id): PlayerChallengeState.Pending,
                                               (PFOUR.id) : PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE.id] == PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR.id] == PlayerChallengeState.Accepted
                }
        }
    }
}
