package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:08 PM
 */
public class PlayerNotSolvingAPuzzleExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Player is not one of the solvers in this game.",
        new PlayerNotSolvingAPuzzleException().getMessage());
  }

}
