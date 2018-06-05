package com.jtbdevelopment.TwistedHangman.game.state.masked;

import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 2/19/15 Time: 6:56 PM
 */
public class MaskedGameTest {

  @Test
  public void testInit() {
    MaskedGame maskedGame = new MaskedGame();

    Assert.assertNull(maskedGame.getWordPhraseSetter());
    Assert.assertTrue(maskedGame.getPlayerRoundScores().isEmpty());
    Assert.assertTrue(maskedGame.getPlayerRunningScores().isEmpty());
    Assert.assertTrue(maskedGame.getSolverStates().isEmpty());
  }

}
