package com.jtbdevelopment.TwistedHangman.game.state;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

/**
 * Date: 10/31/14 Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
public class IndividualGameState implements Serializable {

  public static final int BASE_PENALTIES = 6;
  public static final int GALLOWS_PENALTIES = 3;
  public static final int FACE_PENALTIES = 4;
  private String category;
  @Transient
  private char[] wordPhrase;
  @Transient
  private char[] workingWordPhrase;
  private String wordPhraseString;
  private String workingWordPhraseString;
  private int maxPenalties;
  private int moveCount = 0;
  private int penalties = 0;
  private int blanksRemaining;
  private Set<GameFeature> features;
  private SortedSet<Character> badlyGuessedLetters = new TreeSet<>();
  private SortedSet<Character> guessedLetters = new TreeSet<>();
  private Map<GameFeature, Object> featureData = new HashMap<>();

  public IndividualGameState() {
    this(new HashSet<>());
  }

  public IndividualGameState(final Set<GameFeature> features) {
    this.wordPhrase = new char[0];
    this.workingWordPhrase = new char[0];
    this.wordPhraseString = "";
    this.workingWordPhraseString = "";
    this.category = "";
    this.blanksRemaining = 0;
    this.maxPenalties = BASE_PENALTIES;
    this.features = Collections
        .unmodifiableSet(features != null ? features : Collections.emptySet());
  }

  @PersistenceConstructor
  public IndividualGameState(final Set<GameFeature> features, final String wordPhraseString,
      final String workingWordPhraseString) {
    this.features = features;
    setWorkingWordPhraseString(workingWordPhraseString);
    setWordPhraseString(wordPhraseString);
  }

  public boolean isPuzzleSolved() {
    return Arrays.equals(wordPhrase, workingWordPhrase) && wordPhrase.length > 0;
  }

  public boolean isPlayerHung() {
    return penalties >= maxPenalties;
  }

  public boolean isPuzzleOver() {
    return (isPlayerHung() || isPuzzleSolved());
  }

  public int getPenaltiesRemaining() {
    return maxPenalties - penalties;
  }

  public String getWordPhraseString() {
    return wordPhraseString;
  }

  public void setWordPhraseString(final String wordPhraseString) {
    this.wordPhraseString = wordPhraseString;
    this.wordPhrase = wordPhraseString.toCharArray();
  }

  public String getWorkingWordPhraseString() {
    return workingWordPhraseString;
  }

  public void setWorkingWordPhraseString(final String workingWordPhraseString) {
    this.workingWordPhraseString = workingWordPhraseString;
    this.workingWordPhrase = workingWordPhraseString.toCharArray();
  }

  public char[] getWordPhrase() {
    return wordPhrase;
  }

  public void setWordPhrase(final char[] wordPhrase) {
    this.wordPhrase = wordPhrase;
    this.wordPhraseString = new String(wordPhrase);
  }

  public char[] getWorkingWordPhrase() {
    return workingWordPhrase;
  }

  public void setWorkingWordPhrase(final char[] workingWordPhrase) {
    this.workingWordPhrase = workingWordPhrase;
    this.workingWordPhraseString = new String(workingWordPhrase);
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getMaxPenalties() {
    return maxPenalties;
  }

  public void setMaxPenalties(int maxPenalties) {
    this.maxPenalties = maxPenalties;
  }

  public int getMoveCount() {
    return moveCount;
  }

  public void setMoveCount(int moveCount) {
    this.moveCount = moveCount;
  }

  public int getPenalties() {
    return penalties;
  }

  public void setPenalties(int penalties) {
    this.penalties = penalties;
  }

  public int getBlanksRemaining() {
    return blanksRemaining;
  }

  public void setBlanksRemaining(int blanksRemaining) {
    this.blanksRemaining = blanksRemaining;
  }

  public Set<GameFeature> getFeatures() {
    return features;
  }

  public void setFeatures(Set<GameFeature> features) {
    this.features = features;
  }

  public SortedSet<Character> getBadlyGuessedLetters() {
    return badlyGuessedLetters;
  }

  public void setBadlyGuessedLetters(SortedSet<Character> badlyGuessedLetters) {
    this.badlyGuessedLetters = badlyGuessedLetters;
  }

  public SortedSet<Character> getGuessedLetters() {
    return guessedLetters;
  }

  public void setGuessedLetters(SortedSet<Character> guessedLetters) {
    this.guessedLetters = guessedLetters;
  }

  public Map<GameFeature, Object> getFeatureData() {
    return featureData;
  }

  public void setFeatureData(Map<GameFeature, Object> featureData) {
    this.featureData = featureData;
  }
}
