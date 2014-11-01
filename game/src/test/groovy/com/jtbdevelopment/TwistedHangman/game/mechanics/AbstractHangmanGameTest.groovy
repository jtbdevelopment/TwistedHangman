package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import org.junit.Test

/**
 * Date: 10/26/2014
 * Time: 8:55 PM
 *
 */
abstract class AbstractHangmanGameTest<T extends HangmanGame> extends GroovyTestCase {
    @Test
    public void testInitialGameStateForBasicWord() {
        HangmanGameState gameState = makeGame("cat", "animal", 5).gameState
        assert !gameState.gameOver
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters.empty
        assert gameState.workingWordPhraseString == "___"
        assert gameState.wordPhraseString == "CAT"
        assert gameState.maxPenalties == 5
        assert gameState.penalties == 0
        assert gameState.category == "animal"
        assert gameState.moveCount == 0
        assert gameState.penaltiesRemaining == 5
    }

    @Test
    public void testInitialGameStateForPhraseWithNonAlphabeticCharacters() {
        HangmanGameState gameState = makeGame("It's a wonderful life.", "Movie", 7).gameState
        assert gameState.workingWordPhraseString == "__'_ _ _________ ____."
        assert gameState.wordPhraseString == "IT'S A WONDERFUL LIFE."
        assert gameState.maxPenalties == 7
        assert gameState.penaltiesRemaining == 7
        assert gameState.category == "Movie"
    }

    @Test
    public void testSingleLetterMatchGuess() {
        HangmanGame game = makeGame("Find A Letter", "Junk", 10)
        assert game.guessLetter((char) 'i') == 1;
        HangmanGameState gameState = game.gameState
        assert !gameState.gameOver
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters == new TreeSet(['I'])
        assert gameState.workingWordPhraseString == "_I__ _ ______"
        assert gameState.category == "Junk"
        assert gameState.moveCount == 1
        assert gameState.penaltiesRemaining == 10
    }

    @Test
    public void testMultipleLetterMatchGuess() {
        HangmanGame game = makeGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        HangmanGameState gameState = game.gameState
        assert !gameState.gameOver
        assert gameState.penalties == 0
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters == new TreeSet(['E', 'I'])
        assert gameState.workingWordPhraseString == "_I__ _ _E__E_"
        assert gameState.moveCount == 2
        assert gameState.penaltiesRemaining == 10
    }

    @Test
    public void testLetterMatchGuessFailure() {
        HangmanGame game = makeGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        assert game.guessLetter((char) 'Z') == 0;
        assert game.guessLetter((char) 'X') == 0;
        HangmanGameState gameState = game.gameState
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.badlyGuessedLetters == new TreeSet(['X', 'Z'])
        assert gameState.guessedLetters == new TreeSet(['E', 'I', 'X', 'Z'])
        assert gameState.workingWordPhraseString == "_I__ _ _E__E_"
        assert gameState.moveCount == 4
        assert gameState.penaltiesRemaining == 8
    }

    @Test
    public void testGameLost() {
        HangmanGame game = setUpAlmostFinishedGame()
        game.guessLetter((char) 'T')
        HangmanGameState gameState = game.gameState
        assert !gameState.gameWon
        assert gameState.gameLost
        assert gameState.gameOver

        assert gameState.penalties == gameState.maxPenalties
        assert gameState.workingWordPhraseString == "WI__ER"
        assert gameState.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M', 'T'])
        assert gameState.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'T'])
        assert gameState.moveCount == 9
        assert gameState.penaltiesRemaining == 0
    }

    @Test
    public void testGameWon() {
        HangmanGame game = setUpAlmostFinishedGame()
        game.guessLetter((char) 'N')
        HangmanGameState gameState = game.gameState

        assert gameState.gameWon
        assert !gameState.gameLost
        assert gameState.gameOver

        assert gameState.penalties < gameState.maxPenalties
        assert gameState.workingWordPhraseString == "WINNER"
        assert gameState.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M'])
        assert gameState.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'N'])
        assert gameState.moveCount == 9
        assert gameState.penaltiesRemaining == 1
    }

    protected HangmanGame setUpAlmostFinishedGame() {
        HangmanGame game = makeGame("Winner", "Not a Loser", 5);
        ['w', 'i', 'e', 'r'].each {
            String guess ->
                game.guessLetter(guess.charAt(0))
                assert !game.gameState.isGameWon()
                assert !game.gameState.isGameLost()
                assert !game.gameState.isGameOver()
        }
        ['g', 'x', 'l', 'm'].each {
            String guess ->
                game.guessLetter(guess.charAt(0))
                assert !game.gameState.isGameWon()
                assert !game.gameState.isGameWon()
                assert !game.gameState.isGameOver()
        }
        assert game.gameState.penalties == 4
        assert game.gameState.workingWordPhraseString == "WI__ER"
        game
    }

    protected char[] makeWorkingPhraseFromPhrase(final String phrase) {
        char[] working = new char[phrase.length()]
        for (int i = 0; i < phrase.length(); ++i) {
            if (phrase.charAt(i).isLetter()) {
                working[i] = '_';
            } else {
                working[i] = phrase.charAt(i);
            }
        }
        working
    }

    protected HangmanGameState makeGameState(String wordPhrase, String category, int maxPenalties) {
        new HangmanGameState(
                wordPhrase.toUpperCase().toCharArray(),
                makeWorkingPhraseFromPhrase(wordPhrase),
                category,
                maxPenalties,
                getGameFeatures())
    }

    abstract protected T makeGame(final String wordPhrase, final String category, final int maxPenalties)

    abstract protected Set<HangmanGameState.GameFeatures> getGameFeatures();
}
