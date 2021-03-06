package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.players.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/5/14 Time: 7:24 PM
 */
public class PlayerScoreInitializerTest extends TwistedHangmanTestCase {

  private PlayerScoreInitializer initializer = new PlayerScoreInitializer();

  @Test
  public void testOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }

  @Test
  public void testInitializesScoresToZeroForAllPlayersIfNotSet() {
    final THGame game = new THGame();
    List<Player<ObjectId>> players = Arrays.asList(PONE, PTWO, PTHREE, PFOUR);
    game.setPlayers(players);
    initializer.initializeGame(game);
    players.forEach(player -> {
      assertTrue(game.getPlayerRunningScores().containsKey(player.getId()));
      assertEquals(0, (int) game.getPlayerRunningScores().get(player.getId()));
      assertTrue(game.getPlayerRoundScores().containsKey(player.getId()));
      assertEquals(0, (int) game.getPlayerRoundScores().get(player.getId()));
    });
  }

  @Test
  public void testLeavesExistingScoresAlone() {
    final THGame game = new THGame();
    Map<ObjectId, Integer> players = new HashMap<>();
    players.put(PONE.getId(), 2);
    players.put(PTWO.getId(), 0);
    players.put(PTHREE.getId(), 1);
    players.put(PFOUR.getId(), 0);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR));
    Map<ObjectId, Integer> runningScores = new HashMap<>();
    runningScores.put(PONE.getId(), 2);
    runningScores.put(PTHREE.getId(), 1);
    runningScores.put(PTWO.getId(), 0);
    runningScores.put(PFOUR.getId(), 0);
    game.setPlayerRunningScores(new HashMap<>(runningScores));
    initializer.initializeGame(game);
    players.keySet().forEach(player -> {
      assertTrue(game.getPlayerRunningScores().containsKey(player));
      assertEquals(runningScores.get(player), game.getPlayerRunningScores().get(player));
      assertTrue(game.getPlayerRoundScores().containsKey(player));
      assertEquals(0, (int) game.getPlayerRoundScores().get(player));
    });
  }
}
