package com.jtbdevelopment.TwistedHangman.game

import groovy.transform.CompileStatic

/**
 * Date: 10/25/2014
 * Time: 7:44 PM
 */
@CompileStatic
class ThievingHangmanGameImpl implements ThievingHangmanGame {

    private final HangmanGame hangmanGame
    final boolean[] stolenLetterMarkers;
    private int stolenLettersCount = 0;

    ThievingHangmanGameImpl(final HangmanGame hangmanGame) {
        this.hangmanGame = hangmanGame
        stolenLetterMarkers = new boolean[hangmanGame.wordPhrase.size()];
        for (int i = 0; i < stolenLetterMarkers.length; ++i) {
            stolenLetterMarkers[i] = false;
        }
    }

    void stealLetter(int position) {
        if (hangmanGame.wordPhrase[position] == hangmanGame.workingWordPhrase[position]) {
            throw new IllegalArgumentException(STEALING_KNOWN_LETTER_ERROR)
        }
        if (penaltiesRemaining == 1) {
            throw new IllegalStateException(CANT_STEAL_ON_FINAL_PENALTY_ERROR)
        }

        hangmanGame.revealPosition(position)
        ++stolenLettersCount
        stolenLetterMarkers[position] = true
    }

    @Override
    int getStolenLettersCount() {
        return stolenLettersCount
    }

    @Override
    String getCategory() {
        return hangmanGame.category
    }

    @Override
    String getWordPhrase() {
        return hangmanGame.wordPhrase
    }

    @Override
    String getWorkingWordPhrase() {
        return hangmanGame.workingWordPhrase
    }

    @Override
    int getMaxPenalties() {
        return hangmanGame.maxPenalties
    }

    @Override
    int getPenalties() {
        return hangmanGame.penalties + stolenLettersCount
    }

    @Override
    int getPenaltiesRemaining() {
        return hangmanGame.penaltiesRemaining - stolenLettersCount
    }

    @Override
    SortedSet<Character> getGuessedLetters() {
        return hangmanGame.guessedLetters
    }

    @Override
    SortedSet<Character> getBadlyGuessedLetters() {
        return hangmanGame.badlyGuessedLetters
    }

    @Override
    boolean isGameOver() {
        return (isGameLost() || isGameWon())
    }

    @Override
    boolean isGameWon() {
        return hangmanGame.gameWon
    }

    @Override
    boolean isGameLost() {
        return penaltiesRemaining == 0
    }

    @Override
    void revealPosition(final int position) {
        hangmanGame.revealPosition(position)
    }

    @Override
    int guessLetter(final char letter) {
        return hangmanGame.guessLetter(letter);
    }

    @Override
    int getMoveCount() {
        return hangmanGame.moveCount + stolenLettersCount
    }
}
