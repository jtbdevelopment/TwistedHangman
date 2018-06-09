package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
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
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SingleWinner);

    initializer.initializeGame(game);

    assertEquals("", game.getFeatureData().get(GameFeature.SingleWinner));
  }

  @Test
  public void testOverwritesWinner() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SingleWinner);
    game.getFeatureData().put(GameFeature.SingleWinner, 12);

    initializer.initializeGame(game);

    assertEquals("", game.getFeatureData().get(GameFeature.SingleWinner));
  }

  @Test
  public void testLeavesUnsetIfNotInFeatures() {
    THGame game = new THGame();

    initializer.initializeGame(game);

    assertFalse(game.getFeatureData().containsKey(GameFeature.SingleWinner));
  }
}
