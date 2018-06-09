package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 6:50 AM
 */
public class SinglePlayerInitializerTest extends TwistedHangmanTestCase {

  private SinglePlayerInitializer initializer = new SinglePlayerInitializer();

  @Test
  public void testIgnoresNonSinglePlayer() {
    Set<GameFeature> expected = Sets
        .newHashSet(GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving);
    Game game = new Game();
    game.setPlayers(Arrays.asList(PONE, PTWO));
    game.setFeatures(new HashSet<>(expected));
    initializer.initializeGame(game);
    assertEquals(expected, game.getFeatures());
  }

  @Test
  public void testSetsForSinglePlayer() {
    Set<GameFeature> expected = Sets
        .newHashSet(GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner);
    Game game = new Game();
    game.setPlayers(Collections.singletonList(PONE));
    game.setFeatures(new HashSet<>(expected));
    initializer.initializeGame(game);
    assertEquals(expected, game.getFeatures());
  }

  @Test
  public void testSetsForSinglePlayerWithConflictingOptions() {
    Set<GameFeature> expected = Sets
        .newHashSet(GameFeature.SystemPuzzles, GameFeature.SinglePlayer, GameFeature.SingleWinner,
            GameFeature.TwoPlayer, GameFeature.AlternatingPuzzleSetter);
    Game game = new Game();
    game.setPlayers(Collections.singletonList(PONE));
    game.setFeatures(new HashSet<>(expected));
    initializer.initializeGame(game);
    assertEquals(expected, game.getFeatures());
  }

  @Test
  public void testOrder() {
    assert GameInitializer.EARLY_ORDER == initializer.getOrder();
  }
}
