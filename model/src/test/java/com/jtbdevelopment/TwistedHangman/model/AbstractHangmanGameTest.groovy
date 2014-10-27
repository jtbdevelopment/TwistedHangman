package com.jtbdevelopment.TwistedHangman.model

import org.junit.Test

/**
 * Date: 10/26/2014
 * Time: 8:55 PM
 */
abstract class AbstractHangmanGameTest<T extends HangmanGame> extends GroovyTestCase {
    @Test
    public void testInitialGameStateForBasicWord() {
        HangmanGame game = makeGame("cat", "animal", 5)
        assert !game.gameOver
        assert !game.gameLost
        assert !game.gameWon
        assert game.badlyGuessedLetters.empty
        assert game.guessedLetters.empty
        assert game.workingWordPhrase == "___"
        assert game.wordPhrase == "CAT"
        assert game.maxPenalties == 5
        assert game.penalties == 0
        assert game.category == "ANIMAL"
        assert game.moveCount == 0
        assert game.penaltiesRemaining == 5
    }

    @Test
    public void testInitialGameStateForPhraseWithNonAlphabeticCharacters() {
        HangmanGame game = makeGame("It's a wonderful life.", "Movie", 7)
        assert game.workingWordPhrase == "__'_ _ _________ ____."
        assert game.wordPhrase == "IT'S A WONDERFUL LIFE."
        assert game.maxPenalties == 7
        assert game.penaltiesRemaining == 7
        assert game.category == "MOVIE"
    }

    @Test
    public void testSingleLetterMatchGuess() {
        HangmanGame game = makeGame("Find A Letter", "Junk", 10)
        assert game.guessLetter((char) 'i') == 1;
        assert !game.gameOver
        assert game.badlyGuessedLetters.empty
        assert game.guessedLetters == new TreeSet(['I'])
        assert game.workingWordPhrase == "_I__ _ ______"
        assert game.category == "JUNK"
        assert game.moveCount == 1
        assert game.penaltiesRemaining == 10
    }

    @Test
    public void testMultipleLetterMatchGuess() {
        HangmanGame game = makeGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        assert !game.gameOver
        assert game.penalties == 0
        assert game.badlyGuessedLetters.empty
        assert game.guessedLetters == new TreeSet(['E', 'I'])
        assert game.workingWordPhrase == "_I__ _ _E__E_"
        assert game.moveCount == 2
        assert game.penaltiesRemaining == 10
    }

    @Test
    public void testLetterMatchGuessFailure() {
        HangmanGame game = makeGame("Find A Letter", "JUNK", 10)
        assert game.guessLetter((char) 'I') == 1;
        assert game.guessLetter((char) 'e') == 2;
        assert game.guessLetter((char) 'Z') == 0;
        assert game.guessLetter((char) 'X') == 0;
        assert !game.gameOver
        assert game.penalties == 2
        assert game.badlyGuessedLetters == new TreeSet(['X', 'Z'])
        assert game.guessedLetters == new TreeSet(['E', 'I', 'X', 'Z'])
        assert game.workingWordPhrase == "_I__ _ _E__E_"
        assert game.moveCount == 4
        assert game.penaltiesRemaining == 8
    }

    @Test
    public void testRevealPosition() {
        HangmanGame game = makeGame("Show", "JUNK", 10)
        assert game.workingWordPhrase == "____"
        game.revealPosition(2)
        assert game.workingWordPhrase == "__O_"
    }

    @Test
    public void testExceptionOnRevealingGameOver() {
        HangmanGame game = new HangmanGameImpl("Frog", "Animal", 3)
        game.guessLetter((char) 'X')
        game.guessLetter((char) 'Y')
        game.guessLetter((char) 'Z')
        try {
            game.revealPosition(1)
            fail("Should not get here.")
        } catch (IllegalStateException e) {
            assert e.message == HangmanGame.GAME_OVER_ERROR
        }
    }

    @Test
    public void testExceptionOnNegativePosition() {
        HangmanGame game = makeGame("Frog", "Animal", 3)
        try {
            game.revealPosition(-1)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == HangmanGame.NEGATIVE_POSITION_ERROR
        }
    }

    @Test
    public void testExceptionOnBeyondEndPosition() {
        HangmanGame game = makeGame("Frog", "Animal", 3)
        try {
            game.revealPosition(10)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == HangmanGame.POSITION_BEYOND_END_ERROR
        }
    }

    @Test
    public void testGameLost() {
        HangmanGame game = setUpAlmostFinishedGame()

        game.guessLetter((char) 'T')
        assert !game.gameWon
        assert game.gameLost
        assert game.gameOver

        assert game.penalties == game.maxPenalties
        assert game.workingWordPhrase == "WI__ER"
        assert game.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M', 'T'])
        assert game.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'T'])
        assert game.moveCount == 9
        assert game.penaltiesRemaining == 0
    }

    @Test
    public void testGameWon() {
        HangmanGame game = setUpAlmostFinishedGame()

        game.guessLetter((char) 'N')
        assert game.gameWon
        assert !game.gameLost
        assert game.gameOver

        assert game.penalties < game.maxPenalties
        assert game.workingWordPhrase == "WINNER"
        assert game.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M'])
        assert game.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'N'])
        assert game.moveCount == 9
        assert game.penaltiesRemaining == 1
    }

    protected HangmanGame setUpAlmostFinishedGame() {
        HangmanGame game = makeGame("Winner", "Not a Loser", 5);
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
        assert game.penalties == 4
        assert game.workingWordPhrase == "WI__ER"
        game
    }

    abstract protected T makeGame(final String wordPhrase, final String category, final int maxPenalties)
}
