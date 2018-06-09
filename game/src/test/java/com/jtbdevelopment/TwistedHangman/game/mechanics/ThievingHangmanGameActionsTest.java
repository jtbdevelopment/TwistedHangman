package com.jtbdevelopment.TwistedHangman.game.mechanics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameOverException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingKnownLetterException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingNegativePositionException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingOnFinalPenaltyException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.StealingPositionBeyondEndException;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 10/25/14 Time: 8:21 PM
 */
public class ThievingHangmanGameActionsTest extends AbstractGameActionsTest {

  private ThievingHangmanGameActions thievingHangmanGameActions = new ThievingHangmanGameActions();
  private HangmanGameActions hangmanGameActions = new HangmanGameActions();

  @Override
  protected IndividualGameState makeGameState(final String wordPhrase, final String category,
      final int maxPenalties) {
    IndividualGameState gameState = super.makeGameState(wordPhrase, category, maxPenalties);
    gameState.getFeatureData().put(GameFeature.Thieving, true);
    gameState.getFeatureData().put(GameFeature.ThievingCountTracking, 0);
    gameState.getFeatureData().put(GameFeature.ThievingLetters, new ArrayList());

    gameState.getFeatureData().put(GameFeature.ThievingPositionTracking,
        gameState.getWordPhraseString().chars().mapToObj(c -> false).collect(Collectors.toList()));
    return gameState;
  }

  protected Set<GameFeature> getGameFeatures() {
    return Sets.newHashSet(
        GameFeature.ThievingCountTracking,
        GameFeature.ThievingPositionTracking,
        GameFeature.Thieving);
  }

  @Test
  public void testThievingGameWithoutTheft() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 3);
    hangmanGameActions.guessLetter(gameState, 'F');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'a');
    hangmanGameActions.guessLetter(gameState, 'l');
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleSolved());
    assertFalse(gameState.isPuzzleOver());
    assertEquals(2, gameState.getPenalties());
    assertEquals(1, gameState.getPenaltiesRemaining());
    assertEquals(0, gameState.getFeatureData().get(GameFeature.ThievingCountTracking));
    assertEquals(Arrays.asList(false, false, false, false),
        gameState.getFeatureData(GameFeature.ThievingPositionTracking));
    assertEquals(new ArrayList<>(), gameState.getFeatureData(GameFeature.ThievingLetters));
    assertEquals("FR__", gameState.getWorkingWordPhraseString());
    assertEquals(2, gameState.getBlanksRemaining());
    assertEquals(4, gameState.getMoveCount());
  }

  @Test
  public void testStealingALetter() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 4);
    hangmanGameActions.guessLetter(gameState, 'F');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'a');
    thievingHangmanGameActions.stealLetter(gameState, 3);
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleSolved());
    assertFalse(gameState.isPuzzleOver());
    assertEquals(2, gameState.getPenalties());
    assertEquals(2, gameState.getPenaltiesRemaining());
    assertEquals(1, gameState.getFeatureData().get(GameFeature.ThievingCountTracking));
    assertEquals(Arrays.asList(false, false, false, true),
        gameState.getFeatureData().get(GameFeature.ThievingPositionTracking));
    assertEquals(Collections.singletonList('G'),
        gameState.getFeatureData().get(GameFeature.ThievingLetters));
    assertEquals("FR_G", gameState.getWorkingWordPhraseString());
    assertEquals(1, gameState.getBlanksRemaining());
    assertEquals(
        new TreeSet<>(Arrays.asList('F', 'R', 'A')),
        gameState.getGuessedLetters());
    assertEquals(4, gameState.getMoveCount());
  }

  @Test
  public void testStealingALetterWithMultiplePlaces() {
    IndividualGameState gameState = makeGameState("Elephantine", "Animal", 4);
    hangmanGameActions.guessLetter(gameState, 'p');
    thievingHangmanGameActions.stealLetter(gameState, 2);
    assertFalse(gameState.isPlayerHung());
    assertFalse(gameState.isPuzzleSolved());
    assertFalse(gameState.isPuzzleOver());
    assertEquals(1, gameState.getPenalties());
    assertEquals(3, gameState.getPenaltiesRemaining());
    assertEquals(1, gameState.getFeatureData().get(GameFeature.ThievingCountTracking));
    assertEquals(
        Arrays.asList(true, false, true, false, false, false, false, false, false, false, true),
        gameState.getFeatureData().get(GameFeature.ThievingPositionTracking));
    assertEquals(Collections.singletonList('E'),
        gameState.getFeatureData().get(GameFeature.ThievingLetters));
    assertEquals("E_EP______E", gameState.getWorkingWordPhraseString());
    assertEquals(new TreeSet<>(Collections.singletonList('P')),
        gameState.getGuessedLetters());
    assertEquals(2, gameState.getMoveCount());
    assertEquals(7, gameState.getBlanksRemaining());
  }

  @Test
  public void testStealingToWin() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 3);
    hangmanGameActions.guessLetter(gameState, 'F');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'o');
    thievingHangmanGameActions.stealLetter(gameState, 3);
    assertFalse(gameState.isPlayerHung());
    Assert.assertTrue(gameState.isPuzzleSolved());
    Assert.assertTrue(gameState.isPuzzleOver());
    assertEquals(1, gameState.getPenalties());
    assertEquals(2, gameState.getPenaltiesRemaining());
    assertEquals(1, gameState.getFeatureData().get(GameFeature.ThievingCountTracking));
    assertEquals(Arrays.asList(false, false, false, true),
        gameState.getFeatureData().get(GameFeature.ThievingPositionTracking));
    assertEquals(Collections.singletonList('G'),
        gameState.getFeatureData().get(GameFeature.ThievingLetters));
    assertEquals("FROG", gameState.getWorkingWordPhraseString());
    assertEquals(
        new TreeSet<>(Arrays.asList('F', 'R', 'O')),
        gameState.getGuessedLetters());
    Assert.assertTrue(gameState.getBadlyGuessedLetters().isEmpty());
    assertEquals(4, gameState.getMoveCount());
    assertEquals(0, gameState.getBlanksRemaining());
  }

  @Test(expected = StealingKnownLetterException.class)
  public void testExceptionOnStealingPreviouslyStolenLetter() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 3);
    thievingHangmanGameActions.stealLetter(gameState, 3);
    thievingHangmanGameActions.stealLetter(gameState, 3);
  }

  @Test(expected = StealingKnownLetterException.class)
  public void testExceptionOnStealingPreviouslyGuessedLetter() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 3);
    hangmanGameActions.guessLetter(gameState, 'F');
    thievingHangmanGameActions.stealLetter(gameState, 0);
  }

  @Test(expected = StealingOnFinalPenaltyException.class)
  public void testExceptionOnStealingOnFinalPenalty() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 2);
    hangmanGameActions.guessLetter(gameState, 'F');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'a');
    thievingHangmanGameActions.stealLetter(gameState, 3);
  }

  @Test(expected = StealingNegativePositionException.class)
  public void testStealingNegativePosition() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 2);
    thievingHangmanGameActions.stealLetter(gameState, -1);
  }

  @Test(expected = StealingPositionBeyondEndException.class)
  public void testStealingTooLongPosition() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 2);
    thievingHangmanGameActions.stealLetter(gameState, 4);
  }

  @Test(expected = GameOverException.class)
  public void testStealingLostGame() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'x');
    Assert.assertTrue(gameState.isPlayerHung());
    Assert.assertTrue(gameState.isPuzzleOver());
    thievingHangmanGameActions.stealLetter(gameState, 1);
  }

  @Test(expected = GameOverException.class)
  public void testStealingWonGame() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'f');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'o');
    hangmanGameActions.guessLetter(gameState, 'g');
    Assert.assertTrue(gameState.isPuzzleSolved());
    Assert.assertTrue(gameState.isPuzzleOver());
    thievingHangmanGameActions.stealLetter(gameState, 1);
  }
}
