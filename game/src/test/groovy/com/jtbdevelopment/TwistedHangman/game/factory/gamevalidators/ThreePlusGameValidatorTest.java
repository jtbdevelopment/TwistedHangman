package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import static org.junit.Assert.assertFalse;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 7:07 AM
 */
public class ThreePlusGameValidatorTest extends TwistedHangmanTestCase {

  private ThreePlusGameValidator validator = new ThreePlusGameValidator();

  @Test
  public void testErrorMessage() {
    Assert.assertEquals("Game's 3+ player marker is wrong.", validator.errorMessage());
  }

  @Test
  public void testThreePlayersIsGood() {
    Game game = TwistedHangmanTestCase.makeSimpleGame();
    game.getFeatures().add(GameFeature.ThreePlus);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));

    Assert.assertTrue(validator.validateGame(game));
  }

  @Test
  public void testTwoPlayersWithFlagIsBad() {
    Game game = TwistedHangmanTestCase.makeSimpleGame();
    game.getFeatures().add(GameFeature.ThreePlus);
    game.setPlayers(Arrays.asList(PONE, PTWO));

    assertFalse(validator.validateGame(game));
  }

  @Test
  public void testFourPlayersWithoutFlagIsBad() {
    Game game = TwistedHangmanTestCase.makeSimpleGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFIVE));

    assertFalse(validator.validateGame(game));
  }
}
