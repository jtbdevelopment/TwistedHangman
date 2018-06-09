package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:13 PM
 */
public class StealingPositionBeyondEndExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Can't steal letter after the end of the word/phrase.",
        new StealingPositionBeyondEndException().getMessage());
  }

}
