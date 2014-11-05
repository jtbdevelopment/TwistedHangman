package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 10/25/2014
 * Time: 5:20 PM
 *
 * Game presumes gameState has been uppercased
 */
@CompileStatic
@Component
class HangmanGameActions {
    //  TODO
    public static final String ALREADY_GUESSED_ERROR = "Letter previously guessed.";
    public static final String NOT_A_LETTER_ERROR = "Guess is not a letter.";
    public static final String GAME_OVER_ERROR = "Game is already over.";
    public static final String INVALID_MAX_GUESSES_ERROR = "Invalid maximum guesses.";
    public static final String EMPTY_WORD_PHRASE_ERROR = "Empty word/phrase.";
    public static final String EMPTY_CATEGORY_ERROR = "Category is invalid.";

    //final HangmanGameState gameState

    /*
    public HangmanGame(final HangmanGameState gameState) {
        //  TODO - none of this validation belonged here - move it
        if (gameState.maxPenalties <= 0) {
            throw new IllegalArgumentException(INVALID_MAX_GUESSES_ERROR)
        }
        if (StringUtils.isEmpty(new Wor gameState.wordPhraseAsString)) {
            throw new IllegalArgumentException(EMPTY_WORD_PHRASE_ERROR)
        }
        if (StringUtils.isEmpty(category)) {
            throw new IllegalArgumentException(EMPTY_CATEGORY_ERROR)
        }
        gameState.category = gameState.category.toUpperCase()
        gameState.wordPhrase = new String(gameState.wordPhrase.toUpperCase().toCharArray()
        workingWordPhraseArray = new char[wordPhraseArray.length]

        for (int i = 0; i < wordPhraseArray.length; ++i) {
            if (Character.isLetter(wordPhraseArray[i])) {
                workingWordPhraseArray[i] = '_';
            } else {
                workingWordPhraseArray[i] = wordPhraseArray[i];
            }
        }
        this.gameState = gameState
    }
        */

    public int guessLetter(final IndividualGameState gameState, final char letter) {
        if (gameState.gameOver) {
            throw new IllegalStateException(GAME_OVER_ERROR)
        }
        if (!letter.isLetter()) {
            throw new IllegalArgumentException(NOT_A_LETTER_ERROR);
        }

        char uppercaseLetter = letter.toUpperCase();

        if (gameState.guessedLetters.contains(uppercaseLetter)) {
            throw new IllegalStateException(ALREADY_GUESSED_ERROR)
        }

        int found = 0;
        for (int i = 0; i < gameState.wordPhrase.length; ++i) {
            if (gameState.wordPhrase[i] == uppercaseLetter) {
                gameState.workingWordPhrase[i] = uppercaseLetter;
                ++found;
            }
        }

        if (found == 0) {
            gameState.badlyGuessedLetters.add(uppercaseLetter);
            gameState.penalties += 1
        }
        gameState.guessedLetters.add(uppercaseLetter);
        gameState.moveCount += 1

        return found;
    }
}
