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
    String category;
    String workingWordPhrase
    int maxPenalties;
    int moveCount = 0;
    int penalties = 0;
    Set<GameFeature> features = [] as Set
    SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    SortedSet<Character> guessedLetters = [] as SortedSet
    Map<GameFeature, Object> featureData = [:];  //  Any
    boolean isGameWon;
    boolean isGameLost;
    boolean isGameOver;
    int penaltiesRemaining;
}
