package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/5/14 Time: 7:11 PM
 */
public class ChallengerInitializerTest extends TwistedHangmanTestCase {

  private ChallengerInitializer initializer = new ChallengerInitializer();

  @Test
  public void testOrder() {
    assert GameInitializer.DEFAULT_ORDER == initializer.getOrder();
  }

  @Test
  public void testSystemPuzzles() {
    Map<ObjectId, IndividualGameState> expectedSolvers = new HashMap<>();
    expectedSolvers.put(PONE.getId(), null);
    expectedSolvers.put(PTHREE.getId(), null);
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SystemPuzzles);
    game.setSolverStates(expectedSolvers);
    initializer.initializeGame(game);

    assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId(),
        game.getWordPhraseSetter());
    assertEquals(expectedSolvers, game.getSolverStates());
  }

  @Test
  public void testTwoPlayerSystemPuzzles() {
    Map<ObjectId, IndividualGameState> expectedSolvers = new HashMap<>();
    expectedSolvers.put(PONE.getId(), null);
    expectedSolvers.put(PTHREE.getId(), null);
    THGame game = new THGame();
    game.getFeatures().addAll(Arrays.asList(GameFeature.TwoPlayer, GameFeature.SystemPuzzles));
    game.setSolverStates(expectedSolvers);
    initializer.initializeGame(game);

    assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId(),
        game.getWordPhraseSetter());
    assertEquals(expectedSolvers, game.getSolverStates());
  }

  @Test
  public void testAlternatingChallenger() {
    Map<ObjectId, IndividualGameState> expectedSolvers = new HashMap<>();
    expectedSolvers.put(PTHREE.getId(), null);
    expectedSolvers.put(PONE.getId(), null);

    THGame game = new THGame();
    game.getFeatures().addAll(Arrays.asList(GameFeature.AlternatingPuzzleSetter));
    game.setSolverStates(expectedSolvers);
    game.setPlayers(Arrays.asList(PONE, PTHREE));
    initializer.initializeGame(game);

    assertEquals(PONE.getId(), game.getWordPhraseSetter());
    assertEquals(expectedSolvers, game.getSolverStates());
  }

  @Test
  public void testTwoPlayerAlternatingChallenger() {
    Map<ObjectId, IndividualGameState> expectedSolvers = new HashMap<>();
    expectedSolvers.put(PONE.getId(), null);
    expectedSolvers.put(PTHREE.getId(), null);

    THGame game = new THGame();
    game.getFeatures()
        .addAll(Arrays.asList(GameFeature.AlternatingPuzzleSetter, GameFeature.TwoPlayer));
    game.setSolverStates(expectedSolvers);
    game.setPlayers(Arrays.asList((PONE), (PTHREE)));
    initializer.initializeGame(game);

    assertEquals(PONE.getId(), game.getWordPhraseSetter());
    assertEquals(expectedSolvers, game.getSolverStates());
  }

  @Test
  public void testTwoPlayerSimulataneous() {
    Map<ObjectId, IndividualGameState> expectedSolvers = new HashMap<>();
    expectedSolvers.put(PONE.getId(), null);
    expectedSolvers.put(PTHREE.getId(), null);

    THGame game = new THGame();
    game.getFeatures().addAll(Arrays.asList(GameFeature.TwoPlayer));
    game.setSolverStates(expectedSolvers);
    initializer.initializeGame(game);

    assertNull(game.getWordPhraseSetter());
    assertEquals(expectedSolvers, game.getSolverStates());
  }
}
