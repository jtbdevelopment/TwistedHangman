package com.jtbdevelopment.TwistedHangman.model

import groovy.transform.CompileStatic

/**
 * Date: 10/25/2014
 * Time: 7:44 PM
 */
@CompileStatic
class ThievingHangmanGame extends BasicHangmanGame {
    public static final String STEALING_KNOWN_LETTER_ERROR = "Letter already known at position."
    public static final String POSITION_BEYOND_END_ERROR = "Position is beyond end of word/phrase."
    public static final String NEGATIVE_POSITION_ERROR = "Negative position can't be stolen."
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
            throw new IllegalArgumentException(GAME_OVER_ERROR)
        }
        if (position < 0) {
            throw new IllegalArgumentException(NEGATIVE_POSITION_ERROR)
        }
        if (position >= wordPhraseArray.length) {
            throw new IllegalArgumentException(POSITION_BEYOND_END_ERROR)
        }
        if (super.wordPhraseArray[position] == displayedWordPhraseArray[position]) {
            throw new IllegalArgumentException(STEALING_KNOWN_LETTER_ERROR)
        }

        ++stolenLetters
        stolenLetterMarkers[position] = true
        displayedWordPhraseArray[position] = wordPhraseArray[position];
    }

    public int getStolenLetters() {
        return stolenLetters
    }


}
