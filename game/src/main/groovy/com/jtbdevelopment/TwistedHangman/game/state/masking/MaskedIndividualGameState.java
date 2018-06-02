package com.jtbdevelopment.TwistedHangman.game.state.masking;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Date: 10/31/14 Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
public class MaskedIndividualGameState {

  public String wordPhrase;
  public String workingWordPhrase;
  public SortedSet<Character> badlyGuessedLetters = new TreeSet<>();
  public SortedSet<Character> guessedLetters = new TreeSet<>();
  public Map<GameFeature, Object> featureData = new HashMap<>();
  public String category;
  public int maxPenalties;
  public int moveCount = 0;
  public int penalties = 0;
  public int blanksRemaining = 0;
  public Set<GameFeature> features = new HashSet<>();
  public boolean isPuzzleSolved;
  public boolean isPlayerHung;
  public boolean isPuzzleOver;
  public int penaltiesRemaining;
}
