package com.jtbdevelopment.TwistedHangman.model

import org.junit.Test

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */

//  TODO - test exceptions in constructor and guesser

class BasicHangmanGameTest extends GroovyTestCase {
    @Test
    public void testInitialGameStateForBasicWord() {
        BasicHangmanGame game = new BasicHangmanGame("cat", "animal", 5)
        assert !game.gameOver
        assert !game.gameLost
        assert !game.gameWon
        assert game.badGuessedLetters.empty
        assert game.goodGuessedLetters.empty
        assert game.guessedLetters.empty
        assert game.displayedWordPhrase == "___"
        assert game.wordPhrase == "CAT"
        assert game.guessesUntilHung == 5
        assert game.hangingParts == 0
        assert game.category == "ANIMAL"
    }

    @Test
    public void testInitialGameStateForPhraseWithNonAlphabeticCharacters() {
        BasicHangmanGame game = new BasicHangmanGame("It's a wonderful life.", "Movie", 7)
        assert game.displayedWordPhrase == "__'_ _ _________ ____."
        assert game.wordPhrase == "IT'S A WONDERFUL LIFE."
        assert game.guessesUntilHung == 7
        assert game.category == "MOVIE"
    }

    @Test
    public void testSingleLetterMatchGuess() {
        BasicHangmanGame game = new BasicHangmanGame("Find A Letter", "Junk", 10)
        assert game.guessLetter((char) 'i') == 1;
        assert !game.gameOver
        assert game.badGuessedLetters.empty
        assert game.guessedLetters == new TreeSet(['I'])
        assert game.goodGuessedLetters == new TreeSet(['I'])
        assert game.displayedWordPhrase == "_I__ _ ______"
        assert game.category == "JUNK"
    }

    @Test
    public void testMultipleLetterMatchGuess() {
        BasicHangmanGame game = new BasicHangmanGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        assert !game.gameOver
        assert game.hangingParts == 0
        assert game.badGuessedLetters.empty
        assert game.guessedLetters == new TreeSet(['E', 'I'])
        assert game.goodGuessedLetters == new TreeSet(['E', 'I'])
        assert game.displayedWordPhrase == "_I__ _ _E__E_"
    }

    @Test
    public void testLetterMatchGuessFailure() {
        BasicHangmanGame game = new BasicHangmanGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        assert game.guessLetter((char) 'Z') == 0;
        assert game.guessLetter((char) 'X') == 0;
        assert !game.gameOver
        assert game.hangingParts == 2
        assert game.badGuessedLetters == new TreeSet(['X', 'Z'])
        assert game.guessedLetters == new TreeSet(['E', 'I', 'X', 'Z'])
        assert game.goodGuessedLetters == new TreeSet(['E', 'I'])
        assert game.displayedWordPhrase == "_I__ _ _E__E_"
    }

    @Test
    public void testGameLost() {
        BasicHangmanGame game = setUpAlmostFinishedGame()

        game.guessLetter((char) 'T')
        assert !game.gameWon
        assert game.gameLost
        assert game.gameOver

        assert game.hangingParts == game.guessesUntilHung
        assert game.displayedWordPhrase == "WI__ER"
        assert game.badGuessedLetters == new TreeSet(['G', 'X', 'L', 'M', 'T'])
        assert game.goodGuessedLetters == new TreeSet(['W', 'I', 'E', 'R'])
        assert game.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'T'])
    }

    @Test
    public void testGameWon() {
        BasicHangmanGame game = setUpAlmostFinishedGame()

        game.guessLetter((char) 'N')
        assert game.gameWon
        assert !game.gameLost
        assert game.gameOver

        assert game.hangingParts < game.guessesUntilHung
        assert game.displayedWordPhrase == "WINNER"
        assert game.badGuessedLetters == new TreeSet(['G', 'X', 'L', 'M'])
        assert game.goodGuessedLetters == new TreeSet(['W', 'I', 'N', 'E', 'R'])
        assert game.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'N'])
    }

    private BasicHangmanGame setUpAlmostFinishedGame() {
        BasicHangmanGame game = new BasicHangmanGame("Winner", "Not a Loser", 5);
        ['w', 'i', 'e', 'r'].each {
            String guess ->
                game.guessLetter(guess.charAt(0))
                assert !game.gameWon
                assert !game.gameLost
                assert !game.gameOver
        }
        ['g', 'x', 'l', 'm'].each {
            String guess ->
                game.guessLetter(guess.charAt(0))
                assert !game.gameWon
                assert !game.gameLost
                assert !game.gameOver
        }
        assert game.hangingParts == 4
        assert game.displayedWordPhrase == "WI__ER"
        game
    }
}
