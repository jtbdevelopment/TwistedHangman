package com.jtbdevelopment.TwistedHangman.game.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.PlayerState;
import com.jtbdevelopment.games.state.scoring.GameScorer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/9/14 Time: 9:33 AM
 */
public class GamePhaseTransitionEngineTest extends TwistedHangmanTestCase {

  private GameScorer gameScorer = Mockito.mock(GameScorer.class);
  private GamePhaseTransitionEngine transitionEngine = new GamePhaseTransitionEngine(gameScorer);

  @Test
  public void testSinglePlayerChallengeTransitionsToPlaying() {
    Game game = new Game();

    LinkedHashMap<ObjectId, PlayerState> map = new LinkedHashMap<ObjectId, PlayerState>(1);
    map.put(PONE.getId(), PlayerState.Accepted);

    Map<ObjectId, IndividualGameState> map1 = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setMaxPenalties(10);
    state.setWordPhrase("SET".toCharArray());
    map1.put(PONE.getId(), state);

    game.setGamePhase(GamePhase.Challenged);
    game.setFeatures(Sets.newHashSet(GameFeature.SinglePlayer, GameFeature.SystemPuzzles));
    game.setPlayerStates(map);
    game.setSolverStates(map1);

    assertSame(game, transitionEngine.evaluateGame(game));
    assertEquals(GamePhase.Playing, game.getGamePhase());
  }

  @Test
  public void testSetupToSetup() {
    Game game = new Game();

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setWordPhrase("SETUP".toCharArray());
    state.setMaxPenalties(5);
    map.put(PONE.getId(), state);
    IndividualGameState state1 = new IndividualGameState();
    state1.setMaxPenalties(5);
    map.put(PTWO.getId(), state);

    game.setGamePhase(GamePhase.Setup);
    game.setFeatures(Sets.newHashSet(GameFeature.SystemPuzzles));
    game.setSolverStates(map);

    assertSame(game, transitionEngine.evaluateGame(game));
  }

  @Test
  public void testSetupToPlaying() {
    Game game = new Game();

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setWordPhrase("SETUP".toCharArray());
    state.setMaxPenalties(5);
    map.put(PONE.getId(), state);
    IndividualGameState state1 = new IndividualGameState();
    state1.setWordPhrase("SETUP".toCharArray());
    state1.setMaxPenalties(5);
    map.put(PTWO.getId(), state1);

    game.setGamePhase(GamePhase.Setup);
    game.setFeatures(Sets.newHashSet(GameFeature.SystemPuzzles));
    game.setSolverStates(map);

    assertSame(game, transitionEngine.evaluateGame(game));
    assertEquals(GamePhase.Playing, game.getGamePhase());
  }

  @Test
  public void testPlayingToPlaying() {
    Game game = new Game();

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setWordPhrase("SETUP".toCharArray());
    map.put(PONE.getId(), state);
    IndividualGameState state1 = new IndividualGameState();
    state1.setWordPhrase("SETUP".toCharArray());
    map.put(PTWO.getId(), state1);

    game.setGamePhase(GamePhase.Playing);
    game.setFeatures(new HashSet<>());
    game.setSolverStates(map);

    assertSame(game, transitionEngine.evaluateGame(game));
  }

  @Test
  public void testPlayingToRematchMultipleWinners() {
    Game game = new Game();

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setWordPhrase("SETUP".toCharArray());
    state.setWorkingWordPhrase("SETUP".toCharArray());
    map.put(PONE.getId(), state);
    IndividualGameState state1 = new IndividualGameState();
    state1.setWordPhrase("SETUP".toCharArray());
    state1.setWorkingWordPhrase("SETUP".toCharArray());
    map.put(PTWO.getId(), state1);
    IndividualGameState state2 = new IndividualGameState();
    state2.setWordPhrase("SETUP".toCharArray());
    state2.setPenalties(IndividualGameState.BASE_PENALTIES);
    map.put(PTHREE.getId(), state2);

    game.setId(new ObjectId());
    game.setGamePhase(GamePhase.Playing);
    game.setFeatures(new HashSet<>());
    game.setSolverStates(map);
    Game scored = makeSimpleGame();
    Mockito.when(gameScorer.scoreGame(game)).thenReturn(scored);

    assert DefaultGroovyMethods.is(scored, transitionEngine.evaluateGame(game));
  }

  @Test
  public void testPlayingToRematchSingleWinner() {
    Game game = new Game();

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    IndividualGameState state = new IndividualGameState();
    state.setWordPhrase("SETUP".toCharArray());
    state.setWorkingWordPhrase("SETUP".toCharArray());
    map.put(PONE.getId(), state);
    IndividualGameState state1 = new IndividualGameState();
    state1.setWordPhrase("SETUP".toCharArray());
    state1.setWorkingWordPhrase("SET__".toCharArray());
    map.put(PTWO.getId(), state1);
    IndividualGameState state2 = new IndividualGameState();
    state2.setWordPhrase("SETUP".toCharArray());
    state2.setPenalties(IndividualGameState.BASE_PENALTIES);
    map.put(PTHREE.getId(), state2);

    game.setId(new ObjectId());
    game.setGamePhase(GamePhase.Playing);
    game.setFeatures(Sets.newHashSet(GameFeature.SingleWinner));
    game.setSolverStates(map);
    Game scored = makeSimpleGame();
    Mockito.when(gameScorer.scoreGame(game)).thenReturn(scored);

    assertSame(scored, transitionEngine.evaluateGame(game));
  }
}
