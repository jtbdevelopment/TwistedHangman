package com.jtbdevelopment.TwistedHangman.model

import groovy.transform.CompileStatic
import org.springframework.util.StringUtils

/**
 * Date: 10/25/2014
 * Time: 5:20 PM
 */
@CompileStatic
class HangmanGameImpl implements HangmanGame {
    final int maxPenalties;
    protected final char[] wordPhraseArray;
    protected final char[] workingWordPhraseArray;
    final String category;
    private final SortedSet<Character> badlyGuessedLetters = new TreeSet<>();
    private final SortedSet<Character> guessedLetters = new TreeSet<>();

    public HangmanGameImpl(final String wordPhraseAsString, final String category, int maxPenalties) {
        if (maxPenalties <= 0) {
            throw new IllegalArgumentException(INVALID_MAX_GUESSES_ERROR)
        }
        if (StringUtils.isEmpty(wordPhraseAsString)) {
            throw new IllegalArgumentException(EMPTY_WORD_PHRASE_ERROR)
        }
        if (StringUtils.isEmpty(category)) {
            throw new IllegalArgumentException(EMPTY_CATEGORY_ERROR)
        }
        this.category = category.toUpperCase()
        this.maxPenalties = maxPenalties ?: 0
        this.wordPhraseArray = wordPhraseAsString.toUpperCase().toCharArray()
        workingWordPhraseArray = new char[wordPhraseArray.length]

        for (int i = 0; i < wordPhraseArray.length; ++i) {
            if (Character.isLetter(wordPhraseArray[i])) {
                workingWordPhraseArray[i] = '_';
            } else {
                workingWordPhraseArray[i] = wordPhraseArray[i];
            }
        }
    }

    public void revealPosition(final int position) {
        if (gameOver) {
            throw new IllegalStateException(GAME_OVER_ERROR)
        }
        if (position < 0) {
            throw new IllegalArgumentException(NEGATIVE_POSITION_ERROR)
        }
        if (position >= wordPhraseArray.length) {
            throw new IllegalArgumentException(POSITION_BEYOND_END_ERROR)
        }

        workingWordPhraseArray[position] = wordPhraseArray[position]
    }

    public int guessLetter(final char letter) {
        if (gameOver) {
            throw new IllegalStateException(GAME_OVER_ERROR)
        }
        if (!letter.isLetter()) {
            throw new IllegalArgumentException(NOT_A_LETTER_ERROR);
        }

        char uppercaseLetter = letter.toUpperCase();

        if (guessedLetters.contains(uppercaseLetter)) {
            throw new IllegalArgumentException(ALREADY_GUESSED_ERROR)
        }

        int found = 0;
        for (int i = 0; i < wordPhraseArray.length; ++i) {
            if (wordPhrase[i] == uppercaseLetter) {
                workingWordPhraseArray[i] = uppercaseLetter;
                ++found;
            }
        }

        if (found == 0) {
            badlyGuessedLetters.add(uppercaseLetter);
        }
        guessedLetters.add(uppercaseLetter);

        return found;
    }

    public int getPenaltiesRemaining() {
        return maxPenalties - badlyGuessedLetters.size()
    }

    public int getPenalties() {
        return badlyGuessedLetters.size();
    }

    public boolean isGameWon() {
        return workingWordPhraseArray == wordPhraseArray;
    }

    public boolean isGameLost() {
        return penalties == maxPenalties;
    }

    public boolean isGameOver() {
        return (isGameLost() || isGameWon());
    }

    public String getWordPhrase() {
        return new String(wordPhraseArray);
    }

    public String getWorkingWordPhrase() {
        return new String(workingWordPhraseArray);
    }

    public int getMoveCount() {
        return guessedLetters.size()
    }

    public SortedSet<Character> getGuessedLetters() {
        return Collections.unmodifiableSortedSet(guessedLetters);
    }

    public SortedSet<Character> getBadlyGuessedLetters() {
        return Collections.unmodifiableSortedSet(badlyGuessedLetters);
    }
}
