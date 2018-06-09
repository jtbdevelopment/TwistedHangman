package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 6:56 PM
 */
public class GameIsNotInSetupPhaseExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Can't set puzzle when game is not in setup phase.",
        new GameIsNotInSetupPhaseException().getMessage());
  }

}
