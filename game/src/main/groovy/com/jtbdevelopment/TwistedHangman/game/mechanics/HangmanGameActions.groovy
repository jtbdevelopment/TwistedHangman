package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.exceptions.GameOverException
import com.jtbdevelopment.TwistedHangman.exceptions.LetterAlreadyGuessedException
import com.jtbdevelopment.TwistedHangman.exceptions.NotALetterGuessException
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
    public static int guessLetter(final IndividualGameState gameState, final char letter) {
        char uppercaseLetter = letter.toUpperCase();

        validateGuess(gameState, letter, uppercaseLetter)

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

    protected static void validateGuess(IndividualGameState gameState, char letter, char uppercaseLetter) {
        if (gameState.gameOver) {
            throw new GameOverException()
        }
        if (!letter.isLetter()) {
            throw new NotALetterGuessException()
        }

        if (gameState.guessedLetters.contains(uppercaseLetter)) {
            throw new LetterAlreadyGuessedException()
        }
    }
}
