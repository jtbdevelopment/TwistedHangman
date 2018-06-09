package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 8:58 PM
 */
public class ThievingPositionTrackerPhraseInitializerTest {

  private ThievingPositionTrackerPhraseInitializer initializer = new ThievingPositionTrackerPhraseInitializer();

  @Test
  public void testIgnoresNonThieving() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    gameState.getFeatureData().put(GameFeature.ThievingPositionTracking, "X");
    initializer.initializeForPhrase(gameState);
    assertEquals("X", gameState.getFeatureData().get(GameFeature.ThievingPositionTracking));
  }

  @Test
  public void testSetsThieving() {
    IndividualGameState gameState = new IndividualGameState(Sets.newHashSet(GameFeature.Thieving));
    gameState.getFeatureData().put(GameFeature.ThievingPositionTracking, "X");
    gameState.setWordPhrase("XYZ".toCharArray());
    initializer.initializeForPhrase(gameState);
    assertEquals(
        Arrays.asList(false, false, false),
        gameState.getFeatureData().get(GameFeature.ThievingPositionTracking));
  }
}
