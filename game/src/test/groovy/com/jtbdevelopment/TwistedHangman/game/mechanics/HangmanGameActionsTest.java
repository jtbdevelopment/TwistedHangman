package com.jtbdevelopment.TwistedHangman.game.mechanics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameOverException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.LetterAlreadyGuessedException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.NotALetterGuessException;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

/**
 * Date: 10/25/2014 Time: 6:28 PM
 */
public class HangmanGameActionsTest extends AbstractGameActionsTest {

  private HangmanGameActions hangmanGameActions = new HangmanGameActions();

  @Override
  protected Set<GameFeature> getGameFeatures() {
    return new HashSet<>();
  }

  @Test
  public void testSingleLetterMatchGuess() {
    IndividualGameState gameState = makeGameState("Find A Letter", "Junk", 10);
    assertEquals(1, hangmanGameActions.guessLetter(gameState, 'i'));
    assertFalse(gameState.isPuzzleOver());
    assertTrue(gameState.getBadlyGuessedLetters().isEmpty());
    assertEquals(new TreeSet<>(Collections.singletonList('I')), gameState.getGuessedLetters());
    assertEquals("_I__ _ ______", gameState.getWorkingWordPhraseString());
    assertEquals("Junk", gameState.getCategory());
    assertEquals(1, gameState.getMoveCount());
    assertEquals(10, gameState.getPenaltiesRemaining());
    assertEquals(10, gameState.getBlanksRemaining());
  }

  @Test
  public void testMultipleLetterMatchGuess() {
    IndividualGameState gameState = makeGameState("Find A Letter", "Junk", 10);
    assertEquals(1, hangmanGameActions.guessLetter(gameState, 'I'));
    assertEquals(2, hangmanGameActions.guessLetter(gameState, 'e'));
    assertFalse(gameState.isPuzzleOver());
    assertEquals(0, gameState.getPenalties());
    assertTrue(gameState.getBadlyGuessedLetters().isEmpty());
    assertEquals(new TreeSet<>(Arrays.asList('E', 'I')), gameState.getGuessedLetters());
    assertEquals("_I__ _ _E__E_", gameState.getWorkingWordPhraseString());
    assertEquals(2, gameState.getMoveCount());
    assertEquals(10, gameState.getPenaltiesRemaining());
    assertEquals(8, gameState.getBlanksRemaining());
  }

  @Test
  public void testLetterMatchGuessFailure() {
    IndividualGameState gameState = makeGameState("Find A Letter", "JUNK", 10);
    assertEquals(1, hangmanGameActions.guessLetter(gameState, 'I'));
    assertEquals(2, hangmanGameActions.guessLetter(gameState, 'e'));
    assertEquals(0, hangmanGameActions.guessLetter(gameState, 'Z'));
    assertEquals(0, hangmanGameActions.guessLetter(gameState, 'X'));
    assertFalse(gameState.isPuzzleOver());
    assertEquals(2, gameState.getPenalties());
    assertEquals(new TreeSet<>(Arrays.asList('X', 'Z')), gameState.getBadlyGuessedLetters());
    assertEquals(new TreeSet<>(Arrays.asList('E', 'I', 'X', 'Z')), gameState.getGuessedLetters());
    assertEquals("_I__ _ _E__E_", gameState.getWorkingWordPhraseString());
    assertEquals(4, gameState.getMoveCount());
    assertEquals(8, gameState.getPenaltiesRemaining());
    assertEquals(8, gameState.getBlanksRemaining());
  }

  @Test
  public void testGameLost() {
    IndividualGameState gameState = setUpAlmostFinishedGame();
    hangmanGameActions.guessLetter(gameState, 'T');
    assertFalse(gameState.isPuzzleSolved());
    assertTrue(gameState.isPlayerHung());
    assertTrue(gameState.isPuzzleOver());

    assertEquals(gameState.getMaxPenalties(), gameState.getPenalties());
    assertEquals("WI__ER", gameState.getWorkingWordPhraseString());
    assertEquals(new TreeSet<>(Arrays.asList('G', 'X', 'L', 'M', 'T')),
        gameState.getBadlyGuessedLetters());
    assertEquals(new TreeSet<>(Arrays.asList('W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'T')),
        gameState.getGuessedLetters());
    assertEquals(9, gameState.getMoveCount());
    assertEquals(0, gameState.getPenaltiesRemaining());
    assertEquals(2, gameState.getBlanksRemaining());
  }

  @Test
  public void testGameWon() {
    IndividualGameState gameState = setUpAlmostFinishedGame();
    hangmanGameActions.guessLetter(gameState, 'N');

    assertTrue(gameState.isPuzzleSolved());
    assertFalse(gameState.isPlayerHung());
    assertTrue(gameState.isPuzzleOver());

    assertTrue(gameState.getPenalties() < gameState.getMaxPenalties());
    assertEquals("WINNER", gameState.getWorkingWordPhraseString());
    assertEquals(new TreeSet<>(Arrays.asList('G', 'X', 'L', 'M')),
        gameState.getBadlyGuessedLetters());
    assertEquals(new TreeSet<>(Arrays.asList('W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'N')),
        gameState.getGuessedLetters());
    assertEquals(9, gameState.getMoveCount());
    assertEquals(1, gameState.getPenaltiesRemaining());
    assertEquals(0, gameState.getBlanksRemaining());
  }

  private IndividualGameState setUpAlmostFinishedGame() {
    final IndividualGameState gameState = makeGameState("Winner", "Not a Loser", 5);
    Arrays.asList('w', 'i', 'e', 'r').forEach(letter -> {
      hangmanGameActions.guessLetter(gameState, letter);
      assertFalse(gameState.isPuzzleOver());
      assertFalse(gameState.isPuzzleSolved());
      assertFalse(gameState.isPlayerHung());
    });
    Arrays.asList('g', 'x', 'l', 'm').forEach(letter -> {
      hangmanGameActions.guessLetter(gameState, letter);
      assertFalse(gameState.isPuzzleOver());
      assertFalse(gameState.isPuzzleSolved());
      assertFalse(gameState.isPlayerHung());
    });
    assertEquals(4, gameState.getPenalties());
    assertEquals("WI__ER", gameState.getWorkingWordPhraseString());
    return gameState;
  }

  @Test(expected = GameOverException.class)
  public void testGuessingLostGame() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'x');
    assertTrue(gameState.isPlayerHung());
    assertTrue(gameState.isPuzzleOver());
    hangmanGameActions.guessLetter(gameState, 'e');
  }

  @Test(expected = GameOverException.class)
  public void testGuessingWonGame() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'f');
    hangmanGameActions.guessLetter(gameState, 'r');
    hangmanGameActions.guessLetter(gameState, 'o');
    hangmanGameActions.guessLetter(gameState, 'g');
    assertTrue(gameState.isPuzzleSolved());
    assertTrue(gameState.isPuzzleOver());
    hangmanGameActions.guessLetter(gameState, 'e');
  }

  @Test(expected = LetterAlreadyGuessedException.class)
  public void testDuplicateGuess() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'f');
    hangmanGameActions.guessLetter(gameState, 'f');
  }

  @Test(expected = NotALetterGuessException.class)
  public void testNonLetterGuess() {
    IndividualGameState gameState = makeGameState("Frog", "Animal", 1);
    hangmanGameActions.guessLetter(gameState, 'f');
    hangmanGameActions.guessLetter(gameState, '1');
  }
}
