package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.junit.Test;

/**
 * Date: 11/5/14 Time: 8:08 PM
 */
public class SingleWinnerInitializerTest {

  private SingleWinnerInitializer initializer = new SingleWinnerInitializer();

  @Test
  public void testOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }

  @Test
  public void testInitializesWinner() {
    Game game = new Game();
    game.getFeatures().add(GameFeature.SingleWinner);

    initializer.initializeGame(game);

    assertEquals("", game.getFeatureData().get(GameFeature.SingleWinner));
  }

  @Test
  public void testOverwritesWinner() {
    Game game = new Game();
    game.getFeatures().add(GameFeature.SingleWinner);
    game.getFeatureData().put(GameFeature.SingleWinner, 12);

    initializer.initializeGame(game);

    assertEquals("", game.getFeatureData().get(GameFeature.SingleWinner));
  }

  @Test
  public void testLeavesUnsetIfNotInFeatures() {
    Game game = new Game();

    initializer.initializeGame(game);

    assertFalse(game.getFeatureData().containsKey(GameFeature.SingleWinner));
  }
}
