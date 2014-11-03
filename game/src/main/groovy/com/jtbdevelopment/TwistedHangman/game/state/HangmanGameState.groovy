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
    final String category;
    final char[] wordPhrase;
    char[] workingWordPhrase
    final int maxPenalties;
    int moveCount = 0;
    int penalties = 0;
    final Set<HangmanGameFeatures> features
    final SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    final SortedSet<Character> guessedLetters = [] as SortedSet
    final Map<HangmanGameFeatures, Object> featureData = [:];

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
            final Set<HangmanGameFeatures> features) {
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

    public <T> T getFeatureData(final HangmanGameFeatures feature) {
        (T) featureData[feature]
    }
}
