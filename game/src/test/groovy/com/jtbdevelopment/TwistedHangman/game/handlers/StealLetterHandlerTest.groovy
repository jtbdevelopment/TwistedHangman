package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

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

        try {
            handler.handleActionInternal(PTHREE, game, spot)
            fail()
        } catch (PlayerNotSolvingAPuzzleException e) {

        }
    }


    public void testHandleBadGame() {
        Game game = new Game(gamePhase: GamePhase.Setup)
        int spot = 1
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]

        try {
            handler.handleActionInternal(PTHREE, game, spot)
            fail()
        } catch (GameIsNotInPlayModeException e) {

        }
    }
}
