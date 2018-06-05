package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:02 PM
 */
public class LetterAlreadyGuessedExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Letter previously guessed.", new LetterAlreadyGuessedException().getMessage());
  }

}
