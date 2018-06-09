package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:03 PM
 */
public class NotALetterGuessExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Guess is not a letter.", new NotALetterGuessException().getMessage());
  }

}
