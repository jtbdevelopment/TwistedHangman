package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic

/**
 * Date: 10/31/14
 * Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
@CompileStatic
class HangmanGameState {
    public enum GameFeatures {
        Thieving,
        ThievingCountTracking,
        ThievingPositionTracking
    }

    final String category;
    final char[] wordPhrase;
    char[] workingWordPhrase
    final int maxPenalties;
    int moveCount = 0;
    int penalties = 0;
    final Set<GameFeatures> features
    final SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    final SortedSet<Character> guessedLetters = [] as SortedSet
    final Map<GameFeatures, Object> featureData = [:];

    public HangmanGameState(
            final char[] wordPhrase,
            final char[] workingWordPhrase,
            final String category,
            final int maxPenalties) {
        this(wordPhrase, workingWordPhrase, category, maxPenalties, [] as Set)
    }

    public HangmanGameState(
            final char[] wordPhrase,
            final char[] workingWordPhrase,
            final String category,
            final int maxPenalties,
            final Set<GameFeatures> features) {
        this.wordPhrase = wordPhrase
        this.workingWordPhrase = workingWordPhrase
        this.category = category
        this.maxPenalties = maxPenalties
        this.features = Collections.unmodifiableSet(features ?: Collections.emptySet())
    }

    public boolean isGameWon() {
        wordPhrase == workingWordPhrase;
    }

    public boolean isGameLost() {
        penalties >= maxPenalties;
    }

    public boolean isGameOver() {
        (isGameLost() || isGameWon());
    }

    public int getPenaltiesRemaining() {
        maxPenalties - penalties;
    }

    public String getWordPhraseString() {
        new String(wordPhrase)
    }

    public String getWorkingWordPhraseString() {
        new String(workingWordPhrase)
    }

    public <T> T getFeatureData(final GameFeatures feature) {
        (T) featureData[feature]
    }
}
