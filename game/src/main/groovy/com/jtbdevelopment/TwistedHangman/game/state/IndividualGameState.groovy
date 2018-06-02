package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.annotation.Transient

/**
 * Date: 10/31/14
 * Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
@CompileStatic
class IndividualGameState implements Serializable {
    public static final int BASE_PENALTIES = 6
    public static final int GALLOWS_PENALTIES = 3
    public static final int FACE_PENALTIES = 4

    String category;

    @Transient
    private char[] wordPhrase;
    @Transient
    private char[] workingWordPhrase
    private String wordPhraseString
    private String workingWordPhraseString
    int maxPenalties;
    int moveCount = 0;
    int penalties = 0;
    int blanksRemaining;
    Set<GameFeature> features
    SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    SortedSet<Character> guessedLetters = [] as SortedSet
    Map<GameFeature, Object> featureData = [:];

    IndividualGameState() {
        this([] as Set)
    }

    IndividualGameState(final Set<GameFeature> features) {
        this.wordPhrase = new char[0]
        this.workingWordPhrase = new char[0]
        this.wordPhraseString = ""
        this.workingWordPhraseString = ""
        this.category = ""
        this.blanksRemaining = 0;
        this.maxPenalties = BASE_PENALTIES
        this.features = Collections.unmodifiableSet(features ?: Collections.<GameFeature> emptySet())
    }

    @PersistenceConstructor
    IndividualGameState(
            final Set<GameFeature> features,
            final String wordPhraseString, final String workingWordPhraseString) {
        this.features = features
        setWorkingWordPhraseString(workingWordPhraseString)
        setWordPhraseString(wordPhraseString)
    }

    boolean isPuzzleSolved() {
        wordPhrase == workingWordPhrase && wordPhrase.length > 0
    }

    boolean isPlayerHung() {
        penalties >= maxPenalties;
    }

    boolean isPuzzleOver() {
        (isPlayerHung() || isPuzzleSolved());
    }

    int getPenaltiesRemaining() {
        maxPenalties - penalties;
    }

    String getWordPhraseString() {
        wordPhraseString
    }

    void setWordPhraseString(final String wordPhraseString) {
        this.wordPhraseString = wordPhraseString
        this.wordPhrase = wordPhraseString.toCharArray()
    }

    String getWorkingWordPhraseString() {
        workingWordPhraseString
    }

    void setWorkingWordPhraseString(final String workingWordPhraseString) {
        this.workingWordPhraseString = workingWordPhraseString
        this.workingWordPhrase = workingWordPhraseString.toCharArray()
    }

    char[] getWordPhrase() {
        return wordPhrase
    }

    void setWordPhrase(final char[] wordPhrase) {
        this.wordPhrase = wordPhrase
        this.wordPhraseString = new String(wordPhrase)
    }

    char[] getWorkingWordPhrase() {
        return workingWordPhrase
    }

    void setWorkingWordPhrase(final char[] workingWordPhrase) {
        this.workingWordPhrase = workingWordPhrase
        this.workingWordPhraseString = new String(workingWordPhrase)
    }

    @Override
    String toString() {
        return "IndividualGameState{" +
                "category='" + category + '\'' +
                ", wordPhrase=" + Arrays.toString(wordPhrase) +
                ", workingWordPhrase=" + Arrays.toString(workingWordPhrase) +
                ", maxPenalties=" + maxPenalties +
                ", moveCount=" + moveCount +
                ", penalties=" + penalties +
                ", features=" + features +
                ", badlyGuessedLetters=" + badlyGuessedLetters +
                ", guessedLetters=" + guessedLetters +
                ", featureData=" + featureData +
                ", blanksRemaining=" + blanksRemaining +
                '}';
    }
}
