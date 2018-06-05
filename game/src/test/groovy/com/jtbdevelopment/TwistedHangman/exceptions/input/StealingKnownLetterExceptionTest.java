package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:12 PM
 */
public class StealingKnownLetterExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Can't steal what you already know!",
        new StealingKnownLetterException().getMessage());
  }

}
