package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 11/5/14 Time: 8:11 PM
 */
public class TurnInitializerTest extends TwistedHangmanTestCase {

  private TurnInitializer initializer = new TurnInitializer();

  @Test
  public void testOrder() {
    Assert.assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }

  @Test
  public void testInitializesTurnToFirstPlayer() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.TurnBased);
    game.setPlayers(Arrays.asList(PFOUR, PONE, PTWO, PTHREE));
    initializer.initializeGame(game);

    Assert.assertEquals(PFOUR.getId(), game.getFeatureData(GameFeature.TurnBased));
  }

  @Test
  public void testInitializesTurnToSecondPlayerIfFirstPlayerAlsoPuzzleSetter() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.TurnBased);
    game.setPlayers(Arrays.asList(PFOUR, PONE, PTWO, PTHREE));
    game.setWordPhraseSetter(PFOUR.getId());
    initializer.initializeGame(game);

    Assert.assertEquals(PONE.getId(), game.getFeatureData().get(GameFeature.TurnBased));
  }

  @Test
  public void testNotSetWhenNotAFeature() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));
    initializer.initializeGame(game);

    Assert.assertNull(game.getFeatureData().get(GameFeature.TurnBased));
  }
}
