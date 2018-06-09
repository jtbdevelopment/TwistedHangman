package com.jtbdevelopment.TwistedHangman.game.factory;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/7/14 Time: 8:19 PM
 */
public class GameFactoryTest extends TwistedHangmanTestCase {

  private GameFactory gameFactory = new GameFactory(Collections.emptyList(),
      Collections.emptyList());

  @Test
  public void testCreatingNewGame() {
    assertEquals(Game.class, gameFactory.newGame().getClass());
  }

  @Test
  public void testCreatingRematchGameCopiesRunningScores() {
    Map<ObjectId, Integer> expected = new HashMap<>();
    expected.put(PONE.getId(), 5);
    expected.put(PTWO.getId(), 15);
    Game originalGame = new Game();
    originalGame.setPlayerRunningScores(expected);
    Game newGame = new Game();

    gameFactory.copyFromPreviousGame(originalGame, newGame);
    assertEquals(expected, newGame.getPlayerRunningScores());
  }
}
