package com.jtbdevelopment.TwistedHangman.game.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Date: 11/2/2014 Time: 9:22 PM
 */
public class IndividualGameStateTest {

  @Test
  public void testGameOverWhenGameNotSetYet() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    assertFalse(gameState.isPuzzleSolved());
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleOver());
  }

  @Test
  public void testPersistenceConstructor() {
    Set<GameFeature> features = Sets.newHashSet(GameFeature.TurnBased);
    String wordPhrase = "wp";
    String workingWordPhrase = "__ _";
    IndividualGameState gameState = new IndividualGameState(features, wordPhrase,
        workingWordPhrase);
    assertEquals(features, gameState.getFeatures());
    assertEquals(wordPhrase, gameState.getWordPhraseString());
    assertEquals(workingWordPhrase, gameState.getWorkingWordPhraseString());
  }

  @Test
  public void testInitialGameStateWithDefaultFeatures() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    gameState.setWordPhrase("cat".toCharArray());
    gameState.setWorkingWordPhrase("xxx".toCharArray());
    gameState.setCategory("animal");
    gameState.setMaxPenalties(5);
    assertFalse(gameState.isPuzzleOver());
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleSolved());
    assertTrue(gameState.getBadlyGuessedLetters().isEmpty());
    assertTrue(gameState.getGuessedLetters().isEmpty());
    assertEquals("xxx", gameState.getWorkingWordPhraseString());
    assertEquals("cat", gameState.getWordPhraseString());
    assertEquals(5, gameState.getMaxPenalties());
    assertEquals(0, gameState.getPenalties());
    assertEquals("animal", gameState.getCategory());
    assertEquals(0, gameState.getMoveCount());
    assertEquals(5, gameState.getPenaltiesRemaining());
    assertTrue(gameState.getFeatureData().isEmpty());
    assertTrue(gameState.getFeatures().isEmpty());
  }

  @Test
  public void testInitialGameStateWithFeatures() {
    Set<GameFeature> features = Sets
        .newHashSet(GameFeature.Thieving, GameFeature.ThievingCountTracking);
    IndividualGameState gameState = new IndividualGameState(features);
    gameState.setWordPhrase("CaT".toCharArray());
    gameState.setWorkingWordPhrase("_x_".toCharArray());
    gameState.setCategory("animal");
    gameState.setMaxPenalties(7);
    assertFalse(gameState.isPuzzleOver());
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleSolved());
    assertTrue(gameState.getBadlyGuessedLetters().isEmpty());
    assertTrue(gameState.getGuessedLetters().isEmpty());
    assertEquals("_x_", gameState.getWorkingWordPhraseString());
    assertEquals("CaT", gameState.getWordPhraseString());
    assertEquals(7, gameState.getMaxPenalties());
    assertEquals(0, gameState.getPenalties());
    assertEquals("animal", gameState.getCategory());
    assertEquals(0, gameState.getMoveCount());
    assertEquals(7, gameState.getPenaltiesRemaining());
    assertTrue(gameState.getFeatureData().isEmpty());
    assertEquals(features, gameState.getFeatures());
  }

}
