package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic

/**
 * Date: 10/31/14
 * Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
@CompileStatic
class IndividualGameState {
    public static final int BASE_PENALTIES = 6
    public static final int GALLOWS_PENALTIES = 3
    public static final int FACE_PENALTIES = 3

    String category;
    char[] wordPhrase;
    char[] workingWordPhrase
    final int maxPenalties;
    int moveCount = 0;
    int penalties = 0;
    final Set<GameFeature> features
    final SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    final SortedSet<Character> guessedLetters = [] as SortedSet
    final Map<GameFeature, Object> featureData = [:];

    public IndividualGameState(
            final char[] wordPhrase,
            final char[] workingWordPhrase,
            final String category,
            final int maxPenalties) {
        this(wordPhrase, workingWordPhrase, category, maxPenalties, [] as Set)
    }

    public IndividualGameState(
            final char[] wordPhrase,
            final char[] workingWordPhrase,
            final String category,
            final int maxPenalties,
            final Set<GameFeature> features) {
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

    public <T> T getFeatureData(final GameFeature feature) {
        (T) featureData[feature]
    }
}
