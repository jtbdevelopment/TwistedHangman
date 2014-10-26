package com.jtbdevelopment.TwistedHangman.model

import org.junit.Test

/**
 * Date: 10/25/14
 * Time: 8:21 PM
 */
class ThievingHangmanGameTest extends GroovyTestCase {
    @Test
    public void testThievingGameWithoutTheft() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        game.guessLetter((char) 'l')
        assert !game.gameLost
        assert !game.gameWon
        assert !game.gameOver
        assert game.hangingParts == 2
        assert game.stolenLetters == 0
        assert game.stolenLetterMarkers == [false, false, false, false]
        assert game.displayedWordPhrase == "FR__"
        assert game.moveCount == 4
    }

    @Test
    public void testStealingALetter() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        game.stealLetter(3)
        assert !game.gameLost
        assert !game.gameWon
        assert !game.gameOver
        assert game.hangingParts == 2
        assert game.stolenLetters == 1
        assert game.stolenLetterMarkers == [false, false, false, true]
        assert game.displayedWordPhrase == "FR_G"
        assert game.guessedLetters == new TreeSet(['F', 'R', 'A'])
        assert game.moveCount == 4
    }

    @Test
    public void testStealingToWin() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'o')
        game.stealLetter(3)
        assert !game.gameLost
        assert game.gameWon
        assert game.gameOver
        assert game.hangingParts == 1
        assert game.stolenLetters == 1
        assert game.stolenLetterMarkers == [false, false, false, true]
        assert game.displayedWordPhrase == "FROG"
        assert game.guessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert game.goodGuessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert game.badGuessedLetters.empty
        assert game.moveCount == 4
    }

    @Test
    public void testExceptionOnStealingGameOver() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.guessLetter((char) 'X')
        game.guessLetter((char) 'Y')
        game.guessLetter((char) 'Z')
        try {
            game.stealLetter(1)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.GAME_OVER_ERROR
        }
    }

    @Test
    public void testExceptionOnNegativePosition() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        try {
            game.stealLetter(-1)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.NEGATIVE_POSITION_ERROR
        }
    }

    @Test
    public void testExceptionOnBeyondEndPosition() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        try {
            game.stealLetter(10)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.POSITION_BEYOND_END_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingPreviouslyStolenLetter() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.stealLetter(1)
        try {
            game.stealLetter(1)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingPreviouslyGuessedLetter() {
        ThievingHangmanGame game = new ThievingHangmanGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        try {
            game.stealLetter(0)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.STEALING_KNOWN_LETTER_ERROR
        }
    }
}
