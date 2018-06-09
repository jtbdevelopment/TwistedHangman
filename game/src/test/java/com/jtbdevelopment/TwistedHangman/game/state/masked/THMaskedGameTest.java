package com.jtbdevelopment.TwistedHangman.game.state.masked;

import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 2/19/15 Time: 6:56 PM
 */
public class THMaskedGameTest {

  @Test
  public void testInit() {
    THMaskedGame maskedGame = new THMaskedGame();

    Assert.assertNull(maskedGame.getWordPhraseSetter());
    Assert.assertTrue(maskedGame.getPlayerRoundScores().isEmpty());
    Assert.assertTrue(maskedGame.getPlayerRunningScores().isEmpty());
    Assert.assertTrue(maskedGame.getSolverStates().isEmpty());
  }

}
