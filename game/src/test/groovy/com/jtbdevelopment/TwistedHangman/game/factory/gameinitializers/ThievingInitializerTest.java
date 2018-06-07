package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import jersey.repackaged.com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Date: 11/3/14 Time: 9:29 PM
 */
public class ThievingInitializerTest {

  private ThievingInitializer expander = new ThievingInitializer();

  @Test
  public void testDoesNothingToNoneThievingOptions() {
    Arrays.stream(GameFeature.values()).filter(f -> !GameFeature.Thieving.equals(f))
        .forEach(feature -> {
          Set<GameFeature> expected = Sets.newHashSet(feature);
          Game game = new Game();
          game.setFeatures(new HashSet<>(expected));
          expander.initializeGame(game);
          assertEquals(expected, game.getFeatures());
        });
  }

  @Test
  public void testExpandsThievingOptions() {
    Set<GameFeature> expected = Sets
        .newHashSet(GameFeature.ThievingPositionTracking, GameFeature.Thieving,
            GameFeature.ThievingCountTracking, GameFeature.ThievingLetters);
    Game game = new Game();

    game.getFeatures().add(GameFeature.Thieving);
    expander.initializeGame(game);
    assertEquals(expected, game.getFeatures());
  }

  @Test
  public void testOrder() {
    TestCase.assertEquals(GameInitializer.DEFAULT_ORDER, expander.getOrder());
  }
}
