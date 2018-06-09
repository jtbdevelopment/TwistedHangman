package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotSolvingAPuzzleException;
import com.jtbdevelopment.TwistedHangman.game.mechanics.ThievingHangmanGameActions;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.state.GamePhase;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/10/14 Time: 9:23 PM
 */
public class StealLetterHandlerTest extends TwistedHangmanTestCase {

  private ThievingHangmanGameActions thievingActions = Mockito
      .mock(ThievingHangmanGameActions.class);
  private StealLetterHandler handler = new StealLetterHandler(null, null, null, null, null, null,
      thievingActions);

  @Test
  public void testHandleGoodMove() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Playing);
    int spot = 1;
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);
    handler.handleActionInternal(PONE, game, spot);
    Mockito.verify(thievingActions).stealLetter(state, spot);
  }

  @Test(expected = PlayerNotSolvingAPuzzleException.class)
  public void testHandleBadPlayer() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Playing);
    int spot = 1;
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);

    handler.handleActionInternal(PTHREE, game, spot);
  }

  @Test(expected = GameIsNotInPlayModeException.class)
  public void testHandleBadGame() {
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    int spot = 1;
    IndividualGameState state = new IndividualGameState();

    state.setWordPhrase("Test".toCharArray());
    state.setCategory("Test");
    game.getSolverStates().put(PONE.getId(), state);

    handler.handleActionInternal(PTHREE, game, spot);
  }
}
