package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 7:00 AM
 */
public class MultiplePlayerInitializerTest extends TwistedHangmanTestCase {

  private MultiplePlayerInitializer initializer = new MultiplePlayerInitializer();

  @Test
  public void testExpandsWhenThreePlayers() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));
    initializer.initializeGame(game);
    assertTrue(game.getFeatures().contains(GameFeature.ThreePlus));
  }

  @Test
  public void testExpandsWhenFivePlayers() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR, PFIVE));
    initializer.initializeGame(game);
    assertTrue(game.getFeatures().contains(GameFeature.ThreePlus));
  }

  @Test
  public void testDoesNotExpandsWhenTwoPlayers() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO));
    initializer.initializeGame(game);
    assertFalse(game.getFeatures().contains(GameFeature.ThreePlus));
  }

  @Test
  public void testDoesNotExpandsWhenSinglePlayer() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE));
    initializer.initializeGame(game);
    assertFalse(game.getFeatures().contains(GameFeature.ThreePlus));
  }

  @Test
  public void testOrder() {
    Assert.assertEquals(GameInitializer.EARLY_ORDER, initializer.getOrder());
  }
}
