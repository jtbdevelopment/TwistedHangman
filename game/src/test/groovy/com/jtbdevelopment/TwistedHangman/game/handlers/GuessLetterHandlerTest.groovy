package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.state.GamePhase

/**
 * Date: 11/10/14
 * Time: 9:23 PM
 */
class GuessLetterHandlerTest extends TwistedHangmanTestCase {
    GuessLetterHandler handler = new GuessLetterHandler()


    public void testHandleGoodMove() {
        Game game = new Game(gamePhase: GamePhase.Playing)
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


    public void testHandleBadPlayer() {
        Game game = new Game(gamePhase: GamePhase.Playing)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE): state]

        shouldFail(PlayerNotSolvingAPuzzleException.class, {
            handler.handleActionInternal(PTHREE, game, letter)
        })
    }


    public void testHandleBadGame() {
        Game game = new Game(gamePhase: GamePhase.Setup)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.solverStates = [(PONE): state]

        shouldFail(GameIsNotInPlayModeException.class, {
            handler.handleActionInternal(PTHREE, game, letter)
        })
    }
}
