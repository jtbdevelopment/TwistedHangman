package com.jtbdevelopment.TwistedHangman.model

import org.junit.Test

/**
 * Date: 10/26/2014
 * Time: 9:02 PM
 */
abstract class AbstractThievingHangmanGameTest<T extends ThievingHangmanGame> extends AbstractHangmanGameTest<T> {
    @Test
    public void testThievingGameWithoutTheft() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        game.guessLetter((char) 'l')
        assert !game.gameLost
        assert !game.gameWon
        assert !game.gameOver
        assert game.penalties == 2
        assert game.penaltiesRemaining == 1
        assert game.stolenLettersCount == 0
        assert game.stolenLetterMarkers == [false, false, false, false]
        assert game.workingWordPhrase == "FR__"
        assert game.moveCount == 4
    }

    @Test
    public void testStealingALetter() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 4)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        game.stealLetter(3)
        assert !game.gameLost
        assert !game.gameWon
        assert !game.gameOver
        assert game.penalties == 2
        assert game.penaltiesRemaining == 2
        assert game.stolenLettersCount == 1
        assert game.stolenLetterMarkers == [false, false, false, true]
        assert game.workingWordPhrase == "FR_G"
        assert game.guessedLetters == new TreeSet(['F', 'R', 'A'])
        assert game.moveCount == 4
    }

    @Test
    public void testStealingToWin() {
        ThievingHangmanGame game = new ThievingHangmanGameImpl(new HangmanGameImpl("Frog", "Animal", 3))
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'o')
        game.stealLetter(3)
        assert !game.gameLost
        assert game.gameWon
        assert game.gameOver
        assert game.penalties == 1
        assert game.penaltiesRemaining == 2
        assert game.stolenLettersCount == 1
        assert game.stolenLetterMarkers == [false, false, false, true]
        assert game.workingWordPhrase == "FROG"
        assert game.guessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert game.badlyGuessedLetters.empty
        assert game.moveCount == 4
    }

    @Test
    public void testExceptionOnStealingPreviouslyStolenLetter() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
        game.stealLetter(1)
        try {
            game.stealLetter(1)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGameImpl.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingPreviouslyGuessedLetter() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        try {
            game.stealLetter(0)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGameImpl.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingOnFinalPenalty() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 2)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        try {
            game.stealLetter(3)
            fail("Should not get here")
        } catch (IllegalStateException e) {
            assert e.message == ThievingHangmanGame.CANT_STEAL_ON_FINAL_PENALTY_ERROR
        }
    }
}
