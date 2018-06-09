package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/5/2014 Time: 9:51 PM
 */
public class MaxPenaltiesInitializerTest {

  private MaxPenaltiesInitializer initializer = new MaxPenaltiesInitializer();

  @Test
  public void testOrder() {
    assertEquals(GameInitializer.LATE_ORDER, initializer.getOrder());
  }

  @Test
  public void testBaseGame() {
    THGame game = new THGame();
    game.getFeatures().addAll(Collections.singletonList(GameFeature.Thieving));
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(
        state -> assertEquals(IndividualGameState.BASE_PENALTIES, state.getMaxPenalties()));
  }

  @Test
  public void testDrawBoth() {
    THGame game = new THGame();
    game.getFeatures()
        .addAll(Arrays.asList(GameFeature.Thieving, GameFeature.DrawFace, GameFeature.DrawGallows));
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(state -> assertEquals(
        (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES
            + IndividualGameState.GALLOWS_PENALTIES), state.getMaxPenalties()));
  }

  @Test
  public void testDrawGallows() {
    THGame game = new THGame();
    game.getFeatures()
        .addAll(Arrays.asList(GameFeature.Thieving, GameFeature.DrawGallows));
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(state -> assertEquals(
        (IndividualGameState.BASE_PENALTIES + IndividualGameState.GALLOWS_PENALTIES),
        state.getMaxPenalties()));
  }

  @Test
  public void testDrawFace() {
    THGame game = new THGame();
    game.getFeatures()
        .addAll(Arrays.asList(GameFeature.Thieving, GameFeature.DrawFace));
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);

    initializer.initializeGame(game);
    game.getSolverStates().values().forEach(state -> assertEquals(
        (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES),
        state.getMaxPenalties()));
  }
}
