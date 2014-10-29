package com.jtbdevelopment.TwistedHangman.game;

import java.util.SortedSet;

/**
 * Date: 10/26/2014
 * Time: 3:20 PM
 */
public interface HangmanGame {
    public static final String ALREADY_GUESSED_ERROR = "Letter previously guessed.";
    public static final String NOT_A_LETTER_ERROR = "Guess is not a letter.";
    public static final String GAME_OVER_ERROR = "Game is already over.";
    public static final String INVALID_MAX_GUESSES_ERROR = "Invalid maximum guesses.";
    public static final String EMPTY_WORD_PHRASE_ERROR = "Empty word/phrase.";
    public static final String EMPTY_CATEGORY_ERROR = "Category is invalid.";
    public static final String POSITION_BEYOND_END_ERROR = "Position is beyond end of word/phrase.";
    public static final String NEGATIVE_POSITION_ERROR = "Can't reveal negative position.";

    public String getCategory();

    public String getWordPhrase();

    public String getWorkingWordPhrase();

    public int guessLetter(final char letter);

    public int getMaxPenalties();

    public int getPenalties();

    public int getPenaltiesRemaining();

    public int getMoveCount();

    public SortedSet<Character> getGuessedLetters();

    public SortedSet<Character> getBadlyGuessedLetters();

    public boolean isGameOver();

    public boolean isGameWon();

    public boolean isGameLost();

    //  Largely for variants
    public void revealPosition(final int position);
}
