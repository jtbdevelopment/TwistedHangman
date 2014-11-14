package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.TooLateToRespondToChallenge
import com.jtbdevelopment.TwistedHangman.game.state.Game

/**
 * Date: 11/9/2014
 * Time: 8:21 PM
 */
class ChallengeResponseHandlerTest extends TwistedHangmanTestCase {
    ChallengeResponseHandler handler = new ChallengeResponseHandler()


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


    public void testSetsStateForPlayer() {
        [Game.GamePhase.Declined, Game.GamePhase.Challenge].each {
            Game.GamePhase gamePhase ->
                Game.PlayerChallengeState.findAll { it != Game.PlayerChallengeState.Pending }.each {
                    Game.PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : Game.PlayerChallengeState.Pending,
                                               (PTWO.id)  : Game.PlayerChallengeState.Rejected,
                                               (PTHREE.id): Game.PlayerChallengeState.Pending,
                                               (PFOUR.id) : Game.PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == Game.PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE.id] == Game.PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR.id] == Game.PlayerChallengeState.Accepted
                }
        }
    }


    public void testOverridesResponseForPlayer() {
        [Game.GamePhase.Declined, Game.GamePhase.Challenge].each {
            Game.GamePhase gamePhase ->
                Game.PlayerChallengeState.findAll { it != Game.PlayerChallengeState.Pending }.each {
                    Game.PlayerChallengeState response ->
                        Game game = new Game(
                                gamePhase: gamePhase,
                                playerStates: [(PONE.id)  : Game.PlayerChallengeState.Accepted,
                                               (PTWO.id)  : Game.PlayerChallengeState.Rejected,
                                               (PTHREE.id): Game.PlayerChallengeState.Pending,
                                               (PFOUR.id) : Game.PlayerChallengeState.Accepted,
                                ])
                        handler.handleActionInternal(PONE, game, response)
                        assert game.playerStates[PONE.id] == response
                        assert game.playerStates[PTWO.id] == Game.PlayerChallengeState.Rejected
                        assert game.playerStates[PTHREE.id] == Game.PlayerChallengeState.Pending
                        assert game.playerStates[PFOUR.id] == Game.PlayerChallengeState.Accepted
                }
        }
    }
}
