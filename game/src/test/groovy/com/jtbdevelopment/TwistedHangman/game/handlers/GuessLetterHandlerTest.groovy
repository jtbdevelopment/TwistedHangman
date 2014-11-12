package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.GameIsNotInPlayModeException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 11/10/14
 * Time: 9:23 PM
 */
class GuessLetterHandlerTest extends TwistedHangmanTestCase {
    GuessLetterHandler handler = new GuessLetterHandler()

    @Test
    public void testHandleGoodMove() {
        Game game = new Game(gamePhase: Game.GamePhase.Playing)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE.id): state]
        handler.gameActions = [
                guessLetter: {
                    IndividualGameState s, char c ->
                        assert c == letter
                        assert state.is(s)
                }
        ] as HangmanGameActions

        handler.handleActionInternal(PONE, game, letter)
    }

    @Test
    public void testHandleBadPlayer() {
        Game game = new Game(gamePhase: Game.GamePhase.Playing)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE): state]

        try {
            handler.handleActionInternal(PTHREE, game, letter)
            fail()
        } catch (PlayerNotSolvingAPuzzleException e) {

        }
    }

    @Test
    public void testHandleBadGame() {
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE): state]

        try {
            handler.handleActionInternal(PTHREE, game, letter)
            fail()
        } catch (GameIsNotInPlayModeException e) {

        }
    }
}
