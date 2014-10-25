package com.jtbdevelopment.TwistedHangman.model

import groovy.transform.CompileStatic
import org.springframework.util.StringUtils

/**
 * Date: 10/25/2014
 * Time: 5:20 PM
 */
@CompileStatic
class BasicHangmanGame implements Serializable {
    final int guessesUntilHung;
    final char[] wordPhrase;
    final char[] displayedWordPhrase;
    final String category;
    private final SortedSet<Character> distinctLetters = new TreeSet<>();
    private final SortedSet<Character> goodGuessedLetters = new TreeSet<>();
    private final SortedSet<Character> badGuessedLetters = new TreeSet<>();
    private final SortedSet<Character> guessedLetters = new TreeSet<>();

    public BasicHangmanGame(final String wordPhraseAsString, final String category, int guessesUntilHung) {
        if (guessesUntilHung <= 0) {
            throw new IllegalArgumentException("max guesses must be positive number")
        }
        if (StringUtils.isEmpty(wordPhraseAsString)) {
            throw new IllegalArgumentException("invalid wordPhrase")
        }
        if (StringUtils.isEmpty(category)) {
            throw new IllegalArgumentException("category is invalid")
        }
        this.category = category.toUpperCase()
        this.guessesUntilHung = guessesUntilHung ?: 0
        this.wordPhrase = wordPhraseAsString.toUpperCase().toCharArray()
        displayedWordPhrase = new char[wordPhrase.length]

        for (int i = 0; i < wordPhrase.length; ++i) {
            if (Character.isLetter(wordPhrase[i])) {
                distinctLetters.add(wordPhrase[i]);
                displayedWordPhrase[i] = '_';
            } else {
                displayedWordPhrase[i] = wordPhrase[i];
            }
        }
    }

    public int getHangingParts() {
        return badGuessedLetters.size();
    }

    public boolean isGameWon() {
        return distinctLetters.size() == goodGuessedLetters.size();
    }

    public boolean isGameLost() {
        return getHangingParts() == guessesUntilHung;
    }

    public boolean isGameOver() {
        return (isGameLost() || isGameWon());
    }

    public String getWordPhraseAsString() {
        return new String(wordPhrase);
    }

    public String getDisplayedWordPhraseAsString() {
        return new String(displayedWordPhrase);
    }

    public int guessLetter(final Character letter) {
        if (!letter.isLetter()) {
            throw new IllegalArgumentException("Non alphabetic guess");
        }

        char uppercaseLetter = letter.toUpperCase();
        if (guessedLetters.contains(uppercaseLetter)) {
            throw new IllegalArgumentException("Already guessed this letter")
        }

        int found = 0;
        for (int i = 0; i < wordPhrase.length; ++i) {
            if (wordPhrase[i] == uppercaseLetter) {
                displayedWordPhrase[i] = uppercaseLetter;
                ++found;
            }
        }

        if (found > 0) {
            goodGuessedLetters.add(uppercaseLetter);
        } else {
            badGuessedLetters.add(uppercaseLetter);
        }
        guessedLetters.add(uppercaseLetter);

        return found;
    }

    public SortedSet<Character> getGoodGuessedLetters() {
        return Collections.unmodifiableSortedSet(goodGuessedLetters);
    }

    public SortedSet<Character> getBadGuessedLetters() {
        return Collections.unmodifiableSortedSet(badGuessedLetters);
    }

    public SortedSet<Character> getGuessedLetters() {
        return Collections.unmodifiableSortedSet(guessedLetters);
    }
}
