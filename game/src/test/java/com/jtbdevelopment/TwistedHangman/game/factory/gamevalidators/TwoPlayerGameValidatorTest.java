package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 11/5/2014 Time: 10:12 PM
 */
public class TwoPlayerGameValidatorTest extends TwistedHangmanTestCase {

  private TwoPlayerGameValidator validator = new TwoPlayerGameValidator();

  @Test
  public void testErrorMessage() {
    Assert.assertEquals("Game's two player marker is wrong.", validator.errorMessage());
  }

  @Test
  public void testTwoPlayersIsGood() {
    THGame game = TwistedHangmanTestCase.makeSimpleGame();
    game.getFeatures().add(GameFeature.TwoPlayer);
    game.setPlayers(Arrays.asList(PTWO, PTHREE));

    Assert.assertTrue(validator.validateGame(game));
  }

  @Test
  public void testThreePlayersWithFlagIsGood() {
    THGame game = TwistedHangmanTestCase.makeSimpleGame();
    game.getFeatures().add(GameFeature.TwoPlayer);
    game.setPlayers(Arrays.asList(PTWO, PTHREE, PFOUR));

    Assert.assertFalse(validator.validateGame(game));
  }

  @Test
  public void testTwoPlayersWithoutFlagIsBad() {
    THGame game = TwistedHangmanTestCase.makeSimpleGame();
    game.setPlayers(Arrays.asList(PTWO, PTHREE));

    Assert.assertFalse(validator.validateGame(game));
  }
}
