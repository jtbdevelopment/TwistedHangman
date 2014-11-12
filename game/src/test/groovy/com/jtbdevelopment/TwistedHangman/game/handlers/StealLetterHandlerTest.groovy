package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 11/10/14
 * Time: 9:23 PM
 */
class StealLetterHandlerTest extends TwistedHangmanTestCase {
    StealLetterHandler handler = new StealLetterHandler()

    @Test
    public void testHandleGoodMove() {
        Game game = new Game(gamePhase: Game.GamePhase.Playing)
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

    @Test
    public void testHandleBadPlayer() {
        Game game = new Game(gamePhase: Game.GamePhase.Playing)
        int spot = 1
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]

        try {
            handler.handleActionInternal(PTHREE, game, spot)
            fail()
        } catch (PlayerNotSolvingAPuzzleException e) {

        }
    }

    @Test
    public void testHandleBadGame() {
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
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
