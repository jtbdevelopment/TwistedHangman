package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:12 PM
 */
public class StealingNegativePositionExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Can't steal before beginning of word/phrase.",
        new StealingNegativePositionException().getMessage());
  }

}
