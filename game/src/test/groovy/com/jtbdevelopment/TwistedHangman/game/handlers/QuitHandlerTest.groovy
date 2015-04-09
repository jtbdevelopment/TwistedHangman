package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotPossibleToQuitNowException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState

/**
 * Date: 11/28/2014
 * Time: 7:49 PM
 */
class QuitHandlerTest extends TwistedHangmanTestCase {
    QuitHandler handler = new QuitHandler()

    void testExceptionsOnQuitRematchRematchedPhases() {
        [GamePhase.Quit, GamePhase.RoundOver, GamePhase.NextRoundStarted, GamePhase.Declined].each {
            Game game = new Game(gamePhase: it)
            try {
                handler.handleActionInternal(null, game, null)
                fail()
            } catch (GameIsNotPossibleToQuitNowException e) {
                //
            }
        }
    }

    void testQuitsGamesInOtherStates() {
        [GamePhase.Challenged, GamePhase.Setup, GamePhase.Playing].each {
            Game game = new Game(gamePhase: it, playerStates: [(PONE.id): PlayerState.Pending, (PTWO.id): PlayerState.Rejected])

            Game ret = handler.handleActionInternal(PTWO, game, null)

            assert game.is(ret)
            assert game.gamePhase == GamePhase.Quit
            assert game.playerStates == [(PONE.id): PlayerState.Pending, (PTWO.id): PlayerState.Quit]
        }
    }
}
