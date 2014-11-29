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

    //  Provided for puzzle setter at all times
    //  Provided for other players only at game end
    public String wordPhrase;

    //  Provided for puzzle setter at all times
    //  Only provided for current player during play, provided for all players post-game
    public String workingWordPhrase
    public SortedSet<Character> badlyGuessedLetters = [] as SortedSet
    public SortedSet<Character> guessedLetters = [] as SortedSet
    public Map<GameFeature, Object> featureData = [:];  //  Any

    //  Provided for all players for all other players
    public String category;
    public int maxPenalties;
    public int moveCount = 0;
    public int penalties = 0;
    public Set<GameFeature> features = [] as Set
    public boolean isPuzzleSolved;
    public boolean isPlayerHung;
    public boolean isPuzzleOver;
    public int penaltiesRemaining;
}
