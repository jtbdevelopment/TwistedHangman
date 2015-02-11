package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.TooLateToRespondToChallengeException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.games.games.PlayerState

/**
 * Date: 11/9/2014
 * Time: 8:21 PM
 */
class ChallengeResponseHandlerTest extends TwistedHangmanTestCase {
    ChallengeResponseHandler handler = new ChallengeResponseHandler()

    public void testRequiresEligibilityForAcceptButNotOtherStates() {
        assert handler.requiresEligibilityCheck(PlayerState.Accepted)
        PlayerState.values().findAll { it != PlayerState.Accepted }.each {
            assertFalse handler.requiresEligibilityCheck(it)
        }
    }

    public void testExceptionOnBadPhases() {
        GamePhase.values().findAll { it != GamePhase.Declined && it != GamePhase.Challenged }.each {
            Game game = new Game(gamePhase: it)
            try {
                handler.handleActionInternal(PONE, game, PlayerState.Rejected)
                fail("Should have exceptioned on state " + it)
            } catch (TooLateToRespondToChallengeException e) {
                //
            }
        }
    }


    public void testSetsStateForPlayer() {
        [GamePhase.Declined, GamePhase.Challenged].each {
            GamePhase gamePhase ->
                PlayerState.findAll { it != PlayerState.Pending }.each {
                    PlayerState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : PlayerState.Pending,
                                               (PTWO.id)  : PlayerState.Rejected,
                                               (PTHREE.id): PlayerState.Pending,
                                               (PFOUR.id) : PlayerState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == PlayerState.Rejected
                        assert game.playerStates[PTHREE.id] == PlayerState.Pending
                        assert game.playerStates[PFOUR.id] == PlayerState.Accepted
                }
        }
    }


    public void testOverridesResponseForPlayer() {
        [GamePhase.Declined, GamePhase.Challenged].each {
            GamePhase gamePhase ->
                PlayerState.findAll { it != PlayerState.Pending }.each {
                    PlayerState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : PlayerState.Accepted,
                                               (PTWO.id)  : PlayerState.Rejected,
                                               (PTHREE.id): PlayerState.Pending,
                                               (PFOUR.id) : PlayerState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == PlayerState.Rejected
                        assert game.playerStates[PTHREE.id] == PlayerState.Pending
                        assert game.playerStates[PFOUR.id] == PlayerState.Accepted
                }
        }
    }
}
