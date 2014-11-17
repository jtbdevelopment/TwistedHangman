package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic

/**
 * Date: 10/31/14
 * Time: 6:10 PM
 *
 * Simple state class for transmission and storage of an individual hangman game
 */
@CompileStatic
class MaskedIndividualGameState {
    public String category;
    public String workingWordPhrase
    public int maxPenalties;
    public int moveCount = 0;
    public int penalties = 0;
    public Set<GameFeature> features = [] as Set
    public SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    public SortedSet<Character> guessedLetters = [] as SortedSet
    public Map<GameFeature, Object> featureData = [:];  //  Any
    public boolean isGameWon;
    public boolean isGameLost;
    public boolean isGameOver;
    public int penaltiesRemaining;
}
