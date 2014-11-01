package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
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
        HangmanGameState gameState = game.gameState
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 1
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking] == 0
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking] == [false, false, false, false]
        assert gameState.workingWordPhraseString == "FR__"
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingALetter() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 4)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'a')
        game.stealLetter(3)
        HangmanGameState gameState = game.gameState
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking] == 1
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FR_G"
        assert gameState.guessedLetters == new TreeSet(['F', 'R', 'A'])
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingToWin() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        game.guessLetter((char) 'r')
        game.guessLetter((char) 'o')
        game.stealLetter(3)
        HangmanGameState gameState = game.gameState
        assert !gameState.gameLost
        assert gameState.gameWon
        assert gameState.gameOver
        assert gameState.penalties == 1
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking] == 1
        assert gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FROG"
        assert gameState.guessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert gameState.badlyGuessedLetters.empty
        assert gameState.moveCount == 4
    }

    @Test
    public void testExceptionOnStealingPreviouslyStolenLetter() {
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
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
        ThievingHangmanGame game = makeGame("Frog", "Animal", 3)
        game.guessLetter((char) 'F')
        try {
            game.stealLetter(0)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGame.STEALING_KNOWN_LETTER_ERROR
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
