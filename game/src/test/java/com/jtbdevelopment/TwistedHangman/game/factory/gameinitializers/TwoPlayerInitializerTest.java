package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import jersey.repackaged.com.google.common.collect.Sets;
import org.junit.Test;

/**
 * Date: 11/3/14 Time: 9:42 PM
 */
public class TwoPlayerInitializerTest extends TwistedHangmanTestCase {

  private TwoPlayerInitializer initializer = new TwoPlayerInitializer();

  @Test
  public void testExpandsWhenTwoPlayers() {
    Game game = new Game();
    game.setPlayers(Arrays.asList(PONE, PTWO));
    initializer.initializeGame(game);
    assertEquals(Sets.newHashSet(GameFeature.TwoPlayer), game.getFeatures());
  }

  @Test
  public void testDoesNotExpandsWhenLessThanTwoPlayers() {
    Game game = new Game();
    game.setPlayers(Arrays.asList(PTWO));
    initializer.initializeGame(game);
    assertEquals(Sets.newHashSet(), game.getFeatures());
  }

  @Test
  public void testDoesNotExpandsWhenMoreThanTwoPlayers() {
    Game game = new Game();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));
    initializer.initializeGame(game);
    assertEquals(Sets.newHashSet(), game.getFeatures());
  }

  @Test
  public void testOrder() {
    assertEquals(GameInitializer.EARLY_ORDER, initializer.getOrder());
  }
}
