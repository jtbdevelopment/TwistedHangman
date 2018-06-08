package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.players.Player;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/5/14 Time: 8:00 PM
 */
public class PuzzleInitializerTest extends TwistedHangmanTestCase {

  private PuzzleInitializer initializer = new PuzzleInitializer();

  @Test
  public void testOrder() {
    assert GameInitializer.DEFAULT_ORDER == initializer.getOrder();
  }

  @Test
  public void testInitializesPuzzlesForAllWhenSystemPuzzler() {
    Game game = new Game();
    List<Player<ObjectId>> players = Arrays.asList(PONE, PTWO, PTHREE);
    Set<GameFeature> features = Sets
        .newHashSet(GameFeature.DrawFace, GameFeature.ThievingCountTracking);
    game.setPlayers(players);
    game.getFeatures().addAll(features);
    game.setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId());
    game.getFeatures();
    initializer.initializeGame(game);

    assertEquals(players.size(), game.getSolverStates().size());
    players.forEach(player -> {
      assertTrue(game.getSolverStates().containsKey(player.getId()));
      assertEquals(features, game.getSolverStates().get(player.getId()).getFeatures());
    });
  }

  @Test
  public void testInitializesPuzzlesForTwoPlayerSimultaneous() {
    Game game = new Game();
    List<Player<ObjectId>> players = Arrays.asList(PONE, PTWO, PTHREE);
    Set<GameFeature> features = Sets
        .newHashSet(GameFeature.DrawFace, GameFeature.ThievingCountTracking);
    game.setPlayers(players);
    game.getFeatures().addAll(features);
    game.setWordPhraseSetter(null);
    game.getFeatures();
    initializer.initializeGame(game);

    assertEquals(players.size(), game.getSolverStates().size());
    players.forEach(player -> {
      assertTrue(game.getSolverStates().containsKey(player.getId()));
      assertEquals(features, game.getSolverStates().get(player.getId()).getFeatures());
    });
  }

  @Test
  public void testInitializesPuzzlesForPlayerSetter() {
    Game game = new Game();
    List<Player<ObjectId>> players = Arrays.asList(PONE, PTWO, PTHREE);
    Set<GameFeature> features = Sets
        .newHashSet(GameFeature.DrawFace, GameFeature.ThievingCountTracking);
    game.setPlayers(players);
    game.getFeatures().addAll(features);
    game.setWordPhraseSetter(PTHREE.getId());
    game.getFeatures();
    initializer.initializeGame(game);

    assertEquals(players.size() - 1, game.getSolverStates().size());
    players.forEach(player -> {
      if (PTHREE.equals(player)) {
        assertFalse(game.getSolverStates().containsKey(player.getId()));
      } else {
        assertTrue(game.getSolverStates().containsKey(player.getId()));
        assertEquals(features, game.getSolverStates().get(player.getId()).getFeatures());
      }
    });
  }

  public PuzzleInitializer getInitializer() {
    return initializer;
  }

  public void setInitializer(PuzzleInitializer initializer) {
    this.initializer = initializer;
  }
}
