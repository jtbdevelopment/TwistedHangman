package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 6:55 PM
 */
public class GameOverExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Game is already over.", new GameOverException().getMessage());
  }

}
