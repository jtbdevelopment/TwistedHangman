package com.jtbdevelopment.TwistedHangman.exceptions.input;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

/**
 * Date: 1/13/15 Time: 7:00 PM
 */
public class InvalidPuzzleWordsExceptionTest {

  @Test
  public void testMessage() {
    assertEquals("Your puzzle has invalid words [bal, fud, carr].",
        new InvalidPuzzleWordsException(
            Arrays.asList("bal", "fud", "carr")).getMessage());
  }
}
