package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException;
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGameActions;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.state.GamePhase;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/10/14 Time: 9:23 PM
 */
public class GuessLetterHandlerTest extends TwistedHangmanTestCase {

  private HangmanGameActions gameActions = Mockito.mock(HangmanGameActions.class);
  private GuessLetterHandler handler = new GuessLetterHandler(null, null, null, null, null, null,
      gameActions);

  @Test
  public void testHandleGoodMove() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Playing);
    char letter = 'e';
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);
    handler.handleActionInternal(PONE, game, letter);
    Mockito.verify(gameActions).guessLetter(state, letter);
  }

  @Test(expected = PlayerNotSolvingAPuzzleException.class)
  public void testHandleBadPlayer() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Playing);
    char letter = 'e';
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);

    handler.handleActionInternal(PTHREE, game, letter);
  }

  @Test(expected = GameIsNotInPlayModeException.class)
  public void testHandleBadGame() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    char letter = 'e';
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);

    handler.handleActionInternal(PTHREE, game, letter);
  }
}
