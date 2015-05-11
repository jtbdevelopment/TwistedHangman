package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.state.GamePhase

/**
 * Date: 11/10/14
 * Time: 9:23 PM
 */
class StealLetterHandlerTest extends TwistedHangmanTestCase {
    StealLetterHandler handler = new StealLetterHandler()


    public void testHandleGoodMove() {
        Game game = new Game(gamePhase: GamePhase.Playing)
        int spot = 1
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]
        handler.gameActions = [
                stealLetter: {
                    IndividualGameState s, int p ->
                        assert p == spot
                        assert state.is(s)
                }
        ] as ThievingHangmanGameActions

        handler.handleActionInternal(PONE, game, spot)
    }


    public void testHandleBadPlayer() {
        Game game = new Game(gamePhase: GamePhase.Playing)
        int spot = 1
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]

        shouldFail(PlayerNotSolvingAPuzzleException.class, {
            handler.handleActionInternal(PTHREE, game, spot)
        })
    }


    public void testHandleBadGame() {
        Game game = new Game(gamePhase: GamePhase.Setup)
        int spot = 1
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]

        shouldFail(GameIsNotInPlayModeException.class, {
            handler.handleActionInternal(PTHREE, game, spot)
        })
    }
}
