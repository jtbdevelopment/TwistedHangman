package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import org.junit.Test

/**
 * Date: 10/25/14
 * Time: 8:21 PM
 */
class ThievingHangmanGameActionsTest extends AbstractGameActionsTest {
    private ThievingHangmanGameActions thievingHangmanGameActions = new ThievingHangmanGameActions()
    private HangmanGameActions hangmanGameActions = new HangmanGameActions()

    @Override
    protected HangmanGameState makeGameState(final String wordPhrase, final String category, final int maxPenalties) {
        HangmanGameState gameState = super.makeGameState(wordPhrase, category, maxPenalties)
        gameState.featureData[HangmanGameFeature.Thieving] = true
        gameState.featureData[HangmanGameFeature.ThievingCountTracking] = 0
        gameState.featureData[HangmanGameFeature.ThievingPositionTracking] = (1..(gameState.wordPhrase.length)).collect({
            int c -> false
        }).toArray(new boolean[gameState.wordPhrase.length])
        gameState
    }

    protected Set<HangmanGameFeature> getGameFeatures() {
        return [HangmanGameFeature.ThievingCountTracking, HangmanGameFeature.ThievingPositionTracking, HangmanGameFeature.Thieving] as Set
    }

    @Test
    public void testThievingGameWithoutTheft() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        hangmanGameActions.guessLetter(gameState, (char) 'l')
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 1
        assert gameState.featureData[HangmanGameFeature.ThievingCountTracking] == 0
        assert gameState.featureData[HangmanGameFeature.ThievingPositionTracking] == [false, false, false, false]
        assert gameState.workingWordPhraseString == "FR__"
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingALetter() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 4)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[HangmanGameFeature.ThievingCountTracking] == 1
        assert gameState.featureData[HangmanGameFeature.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FR_G"
        assert gameState.guessedLetters == new TreeSet(['F', 'R', 'A'])
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingToWin() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'o')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assert !gameState.gameLost
        assert gameState.gameWon
        assert gameState.gameOver
        assert gameState.penalties == 1
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[HangmanGameFeature.ThievingCountTracking] == 1
        assert gameState.featureData[HangmanGameFeature.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FROG"
        assert gameState.guessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert gameState.badlyGuessedLetters.empty
        assert gameState.moveCount == 4
    }

    @Test
    public void testExceptionOnStealingPreviouslyStolenLetter() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 3)
        thievingHangmanGameActions.stealLetter(gameState, 3)
        try {
            thievingHangmanGameActions.stealLetter(gameState, 3)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGameActions.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingPreviouslyGuessedLetter() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        try {
            thievingHangmanGameActions.stealLetter(gameState, 0)
            fail("Should not get here.")
        } catch (IllegalArgumentException e) {
            assert e.message == ThievingHangmanGameActions.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingOnFinalPenalty() {
        HangmanGameState gameState = makeGameState("Frog", "Animal", 2)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        try {
            thievingHangmanGameActions.stealLetter(gameState, 3)
            fail("Should not get here")
        } catch (IllegalStateException e) {
            assert e.message == ThievingHangmanGameActions.CANT_STEAL_ON_FINAL_PENALTY_ERROR
        }
    }

}
