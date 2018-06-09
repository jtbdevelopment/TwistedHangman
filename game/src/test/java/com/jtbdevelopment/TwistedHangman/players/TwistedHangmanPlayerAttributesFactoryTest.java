package com.jtbdevelopment.TwistedHangman.players;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes;
import org.junit.Test;

/**
 * Date: 2/2/15 Time: 5:59 PM
 */
public class TwistedHangmanPlayerAttributesFactoryTest {

  private TwistedHangmanPlayerAttributesFactory factory = new TwistedHangmanPlayerAttributesFactory();

  @Test
  public void testNewPlayer() {
    GameSpecificPlayerAttributes attributes = factory.newPlayerAttributes();
    assertNotNull(attributes);
    assertTrue(attributes instanceof TwistedHangmanPlayerAttributes);
  }

  @Test
  public void testNewManualPlayer() {
    GameSpecificPlayerAttributes attributes = factory.newManualPlayerAttributes();
    assertNotNull(attributes);
    assertTrue(attributes instanceof TwistedHangmanPlayerAttributes);
  }

  @Test
  public void testNewSystemPlayer() {
    assertNull(factory.newSystemPlayerAttributes());
  }
}
