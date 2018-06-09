package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/5/2014 Time: 9:48 PM
 */
public class ThievingPuzzleStateInitializerTest {

  private ThievingPuzzleStateInitializer initializer = new ThievingPuzzleStateInitializer();

  @Test
  public void testInitializedThievingVariables() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.Thieving);
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(),
        new IndividualGameState(Collections.emptySet()));
    map.put(new ObjectId(),
        new IndividualGameState(Collections.emptySet()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(state -> {
      assertEquals(0, (int) state.getFeatureData(GameFeature.ThievingCountTracking));
      assertEquals(Collections.emptyList(),
          state.getFeatureData(GameFeature.ThievingPositionTracking));
      assertEquals(Collections.emptyList(),
          state.getFeatureData(GameFeature.ThievingLetters));
    });
  }

  @Test
  public void testNoInitializedIfNoThievingVariables() {
    THGame game = new THGame();
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(),
        new IndividualGameState(Collections.emptySet()));
    map.put(new ObjectId(),
        new IndividualGameState(Collections.emptySet()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(state -> {
      assertNull(state.getFeatureData().get(GameFeature.ThievingCountTracking));
      assertNull(state.getFeatureData().get(GameFeature.ThievingPositionTracking));
      assertNull(state.getFeatureData().get(GameFeature.ThievingLetters));
    });
  }

  @Test
  public void testOrder() {
    assertEquals(GameInitializer.LATE_ORDER, initializer.getOrder());
  }
}
