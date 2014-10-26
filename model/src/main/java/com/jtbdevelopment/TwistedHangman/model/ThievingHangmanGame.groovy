package com.jtbdevelopment.TwistedHangman.model

import groovy.transform.CompileStatic

/**
 * Date: 10/25/2014
 * Time: 7:44 PM
 */
@CompileStatic
class ThievingHangmanGame extends BasicHangmanGame {
    final boolean[] stolenLetterMarkers;
    private int stolenLetters = 0;

    ThievingHangmanGame(final String wordPhraseAsString, final String category, final int guessesUntilHung) {
        super(wordPhraseAsString, category, guessesUntilHung)
        stolenLetterMarkers = new boolean[wordPhraseArray.length];
        for (int i = 0; i < stolenLetterMarkers.length; ++i) {
            stolenLetterMarkers[i] = false;
        }
    }

    boolean[] getStolenLetterMarkers() {
        return (boolean[]) stolenLetterMarkers.clone()
    }

    @Override
    int getHangingParts() {
        return super.getHangingParts() + stolenLetters
    }

    @Override
    boolean isGameLost() {
        return (stolenLetters + badGuessedLetters.size()) == guessesUntilHung
    }

    @Override
    int getMoveCount() {
        return stolenLetters + super.getMoveCount()
    }

    void stealLetter(int position) {
        if (gameOver) {
            throw new IllegalArgumentException("game already over")
        }
        if (position >= wordPhraseArray.length || position < 0) {
            throw new IllegalArgumentException("invalid position")
        }
        if (super.wordPhraseArray[position] == displayedWordPhraseArray[position]) {
            throw new IllegalArgumentException("alread known letter")
        }

        ++stolenLetters
        stolenLetterMarkers[position] = true
        displayedWordPhraseArray[position] = wordPhraseArray[position];
    }

    public int getStolenLetters() {
        return stolenLetters
    }


}
