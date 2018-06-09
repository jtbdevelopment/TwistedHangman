package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 11/5/2014 Time: 10:12 PM
 */
public class SinglePlayerGameValidatorTest extends TwistedHangmanTestCase {

  private SinglePlayerGameValidator validator = new SinglePlayerGameValidator();

  @Test
  public void testErrorMessage() {
    Assert.assertEquals("Game's single player marker is wrong.", validator.errorMessage());
  }

  @Test
  public void testSinglePlayersIsGood() {
    Game game = makeSimpleGame();

    game.getFeatures().add(GameFeature.SinglePlayer);
    game.setPlayers(Collections.singletonList(PONE));

    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testThreePlayersWithFlagIsGood() {
    Game game = makeSimpleGame();

    game.getFeatures().add(GameFeature.SinglePlayer);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));

    assertFalse(validator.validateGame(game));
  }

  @Test
  public void testSinglePlayerWithoutFlagIsBad() {
    Game game = new Game();

    game.setPlayers(Collections.singletonList(PONE));

    assertFalse(validator.validateGame(game));
  }
}
