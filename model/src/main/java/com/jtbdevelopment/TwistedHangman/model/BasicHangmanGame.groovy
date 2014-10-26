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
    protected final char[] wordPhraseArray;
    protected final char[] displayedWordPhraseArray;
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
        this.wordPhraseArray = wordPhraseAsString.toUpperCase().toCharArray()
        displayedWordPhraseArray = new char[wordPhraseArray.length]

        for (int i = 0; i < wordPhraseArray.length; ++i) {
            if (Character.isLetter(wordPhraseArray[i])) {
                distinctLetters.add(wordPhraseArray[i]);
                displayedWordPhraseArray[i] = '_';
            } else {
                displayedWordPhraseArray[i] = wordPhraseArray[i];
            }
        }
    }

    public int getHangingParts() {
        return badGuessedLetters.size();
    }

    public boolean isGameWon() {
        return displayedWordPhrase == wordPhrase;
    }

    public boolean isGameLost() {
        return getHangingParts() == guessesUntilHung;
    }

    public boolean isGameOver() {
        return (isGameLost() || isGameWon());
    }

    public String getWordPhrase() {
        return new String(wordPhraseArray);
    }

    public String getDisplayedWordPhrase() {
        return new String(displayedWordPhraseArray);
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
        for (int i = 0; i < wordPhraseArray.length; ++i) {
            if (wordPhrase[i] == uppercaseLetter) {
                displayedWordPhraseArray[i] = uppercaseLetter;
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
