package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 6:45 PM
 */
public class StealingOnFinalPenaltyExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Can't steal a letter with only one penalty left.",
        new StealingOnFinalPenaltyException().getMessage());
  }

}
