package com.jtbdevelopment.TwistedHangman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import org.junit.Test;

/**
 * Date: 5/30/18 Time: 6:51 AM
 */
public class StringToGameFeatureConverterTest {

  private StringToGameFeatureConverter converter = new StringToGameFeatureConverter();

  @Test
  public void testConverts() {
    assertEquals(GameFeature.DrawGallows, converter.convert("DrawGallows"));
  }

  @Test
  public void testIgnoresNull() {
    assertNull(converter.convert(null));
  }
}
