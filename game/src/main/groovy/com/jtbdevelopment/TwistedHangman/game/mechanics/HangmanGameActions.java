package com.jtbdevelopment.TwistedHangman.game.mechanics;

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameOverException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.LetterAlreadyGuessedException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.NotALetterGuessException;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.stereotype.Component;

/**
 * Date: 10/25/2014 Time: 5:20 PM
 *
 * Game presumes gameState has been uppercased
 */
@Component
public class HangmanGameActions {

  public int guessLetter(final IndividualGameState gameState, final char letter) {
    char uppercaseLetter = DefaultGroovyMethods.toUpperCase(letter);

    validateGuess(gameState, letter, uppercaseLetter);

    int found = 0;
    for (int i = 0; i < gameState.getWordPhrase().length; i = ++i) {
      if (gameState.getWordPhrase()[i] == uppercaseLetter) {
        gameState.getWorkingWordPhrase()[i] = uppercaseLetter;
        gameState.setWorkingWordPhraseString(new String(gameState.getWorkingWordPhrase()));
        found = ++found;
      }

    }

    if (found == 0) {
      gameState.getBadlyGuessedLetters().add(uppercaseLetter);
      gameState.setPenalties(gameState.getPenalties() + 1);
    }

    gameState.getGuessedLetters().add(uppercaseLetter);
    gameState.setMoveCount(gameState.getMoveCount() + 1);
    gameState.setBlanksRemaining(gameState.getBlanksRemaining() - found);

    return found;
  }

  private void validateGuess(IndividualGameState gameState, char letter, char uppercaseLetter) {
    if (gameState.isPuzzleOver()) {
      throw new GameOverException();
    }

    if (!DefaultGroovyMethods.isLetter(letter)) {
      throw new NotALetterGuessException();
    }

    if (gameState.getGuessedLetters().contains(uppercaseLetter)) {
      throw new LetterAlreadyGuessedException();
    }

  }

}
