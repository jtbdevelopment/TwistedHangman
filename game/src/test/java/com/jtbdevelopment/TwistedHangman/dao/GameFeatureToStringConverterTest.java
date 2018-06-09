package com.jtbdevelopment.TwistedHangman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import org.junit.Test;

/**
 * Date: 5/30/18 Time: 6:53 AM
 */
public class GameFeatureToStringConverterTest {

  private GameFeatureToStringConverter converter = new GameFeatureToStringConverter();

  @Test
  public void testConverts() {
    assertEquals("DrawFace", converter.convert(GameFeature.DrawFace));
  }

  @Test
  public void testConvertsNull() {
    assertNull(converter.convert(null));
  }
}
