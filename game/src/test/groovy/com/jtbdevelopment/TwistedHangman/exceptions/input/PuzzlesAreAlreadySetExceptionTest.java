package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:10 PM
 */
public class PuzzlesAreAlreadySetExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("You have already set your puzzles.",
        new PuzzlesAreAlreadySetException().getMessage());
  }

}
