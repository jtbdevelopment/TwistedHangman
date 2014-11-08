package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.exceptions.*
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 10/25/14
 * Time: 8:21 PM
 */
class ThievingHangmanGameActionsTest extends AbstractGameActionsTest {
    private ThievingHangmanGameActions thievingHangmanGameActions = new ThievingHangmanGameActions()
    private HangmanGameActions hangmanGameActions = new HangmanGameActions()

    @Override
    protected IndividualGameState makeGameState(
            final String wordPhrase, final String category, final int maxPenalties) {
        IndividualGameState gameState = super.makeGameState(wordPhrase, category, maxPenalties)
        gameState.featureData[GameFeature.Thieving] = true
        gameState.featureData[GameFeature.ThievingCountTracking] = 0
        gameState.featureData[GameFeature.ThievingPositionTracking] = (1..(gameState.wordPhrase.length)).collect({
            int c -> false
        }).toArray(new boolean[gameState.wordPhrase.length])
        gameState
    }

    protected Set<GameFeature> getGameFeatures() {
        return [GameFeature.ThievingCountTracking, GameFeature.ThievingPositionTracking, GameFeature.Thieving] as Set
    }

    @Test
    public void testThievingGameWithoutTheft() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        hangmanGameActions.guessLetter(gameState, (char) 'l')
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 1
        assert gameState.featureData[GameFeature.ThievingCountTracking] == 0
        assert gameState.featureData[GameFeature.ThievingPositionTracking] == [false, false, false, false]
        assert gameState.workingWordPhraseString == "FR__"
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingALetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 4)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert !gameState.gameOver
        assert gameState.penalties == 2
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[GameFeature.ThievingCountTracking] == 1
        assert gameState.featureData[GameFeature.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FR_G"
        assert gameState.guessedLetters == new TreeSet(['F', 'R', 'A'])
        assert gameState.moveCount == 4
    }

    @Test
    public void testStealingToWin() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'o')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assert !gameState.gameLost
        assert gameState.gameWon
        assert gameState.gameOver
        assert gameState.penalties == 1
        assert gameState.penaltiesRemaining == 2
        assert gameState.featureData[GameFeature.ThievingCountTracking] == 1
        assert gameState.featureData[GameFeature.ThievingPositionTracking] == [false, false, false, true]
        assert gameState.workingWordPhraseString == "FROG"
        assert gameState.guessedLetters == new TreeSet<>(['F', 'R', 'O'])
        assert gameState.badlyGuessedLetters.empty
        assert gameState.moveCount == 4
    }

    @Test
    public void testExceptionOnStealingPreviouslyStolenLetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        thievingHangmanGameActions.stealLetter(gameState, 3)
        try {
            thievingHangmanGameActions.stealLetter(gameState, 3)
            fail("Should not get here.")
        } catch (StealingKnownLetterException e) {
            assert e.message == StealingKnownLetterException.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingPreviouslyGuessedLetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        try {
            thievingHangmanGameActions.stealLetter(gameState, 0)
            fail("Should not get here.")
        } catch (StealingKnownLetterException e) {
            assert e.message == StealingKnownLetterException.STEALING_KNOWN_LETTER_ERROR
        }
    }

    @Test
    public void testExceptionOnStealingOnFinalPenalty() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        try {
            thievingHangmanGameActions.stealLetter(gameState, 3)
            fail("Should not get here")
        } catch (StealingOnFinalPenaltyException e) {
            assert e.message == StealingOnFinalPenaltyException.CANT_STEAL_ON_FINAL_PENALTY_ERROR
        }
    }

    @Test
    public void testStealingNegativePosition() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        try {
            thievingHangmanGameActions.stealLetter(gameState, -1)
            fail("Should not get here")
        } catch (StealingNegativePositionException e) {
            assert StealingNegativePositionException.NEGATIVE_POSITION_ERROR == e.message
        }
    }

    @Test
    public void testStealingTooLongPosition() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        try {
            thievingHangmanGameActions.stealLetter(gameState, 4)
            fail("Should not get here")
        } catch (StealingPositionBeyondEndException e) {
            assert StealingPositionBeyondEndException.POSITION_BEYOND_END_ERROR == e.message
        }
    }

    @Test
    public void testStealingLostGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "x")
        assert gameState.gameLost
        assert gameState.gameOver
        try {
            thievingHangmanGameActions.stealLetter(gameState, 1)
            fail("Should not get here")
        } catch (GameOverException e) {
            assert GameOverException.GAME_OVER_ERROR == e.message
        }
    }

    @Test
    public void testStealingWonGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "f")
        hangmanGameActions.guessLetter(gameState, (char) "r")
        hangmanGameActions.guessLetter(gameState, (char) "o")
        hangmanGameActions.guessLetter(gameState, (char) "g")
        assert gameState.gameWon
        assert gameState.gameOver
        try {
            thievingHangmanGameActions.stealLetter(gameState, 1)
            fail("Should not get here")
        } catch (GameOverException e) {
            assert GameOverException.GAME_OVER_ERROR == e.message
        }
    }
}
