package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.state.GamePhase
import org.junit.Test
import org.mockito.Mockito

/**
 * Date: 11/10/14
 * Time: 9:23 PM
 */
class GuessLetterHandlerTest extends TwistedHangmanTestCase {
    private HangmanGameActions gameActions = Mockito.mock(HangmanGameActions.class)
    private GuessLetterHandler handler = new GuessLetterHandler(null, null, null, null, null, null, gameActions)

    @Test
    void testHandleGoodMove() {
        Game game = new Game(gamePhase: GamePhase.Playing)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.getSolverStates().put(PONE.id, state)
        handler.handleActionInternal(PONE, game, letter)
        Mockito.verify(gameActions).guessLetter(state, letter)
    }

    @Test(expected = PlayerNotSolvingAPuzzleException.class)
    void testHandleBadPlayer() {
        Game game = new Game(gamePhase: GamePhase.Playing)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.getSolverStates().put(PONE.id, state)

        handler.handleActionInternal(PTHREE, game, letter)
    }

    @Test(expected = GameIsNotInPlayModeException.class)
    void testHandleBadGame() {
        Game game = new Game(gamePhase: GamePhase.Setup)
        char letter = 'e'
        IndividualGameState state = new IndividualGameState(wordPhrase: "Test", category: "Test")
        game.getSolverStates().put(PONE.id, state)

        handler.handleActionInternal(PTHREE, game, letter)
    }
}
