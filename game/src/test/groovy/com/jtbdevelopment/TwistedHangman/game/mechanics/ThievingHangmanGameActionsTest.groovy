package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.exceptions.input.*
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

import static org.junit.Assert.*

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
        gameState.featureData[GameFeature.ThievingLetters] = []
        gameState.featureData[GameFeature.ThievingPositionTracking] = (1..(gameState.wordPhrase.length)).collect({
            int c -> false
        })
        gameState
    }

    protected Set<GameFeature> getGameFeatures() {
        return [GameFeature.ThievingCountTracking, GameFeature.ThievingPositionTracking, GameFeature.Thieving] as Set
    }

    @Test
    void testThievingGameWithoutTheft() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        hangmanGameActions.guessLetter(gameState, (char) 'l')
        assertFalse gameState.playerHung
        assertFalse gameState.puzzleSolved
        assertFalse gameState.puzzleOver
        assertEquals 2, gameState.penalties
        assertEquals 1, gameState.penaltiesRemaining
        assertEquals 0, gameState.featureData[GameFeature.ThievingCountTracking]
        assertEquals Arrays.asList(false, false, false, false), gameState.featureData[GameFeature.ThievingPositionTracking]
        assertEquals([], gameState.featureData[GameFeature.ThievingLetters])
        assertEquals "FR__", gameState.workingWordPhraseString
        assertEquals 2, gameState.blanksRemaining
        assertEquals 4, gameState.moveCount
    }

    @Test
    void testStealingALetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 4)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assertFalse gameState.playerHung
        assertFalse gameState.puzzleSolved
        assertFalse gameState.puzzleOver
        assertEquals 2, gameState.penalties
        assertEquals 2, gameState.penaltiesRemaining
        assertEquals 1, gameState.featureData[GameFeature.ThievingCountTracking]
        assertEquals Arrays.asList(false, false, false, true), gameState.featureData[GameFeature.ThievingPositionTracking]
        assertEquals([(char) 'G'], gameState.featureData[GameFeature.ThievingLetters])
        assertEquals "FR_G", gameState.workingWordPhraseString
        assertEquals 1, gameState.blanksRemaining
        assertEquals new TreeSet<>([(char) 'F', (char) 'R', (char) 'A']), gameState.guessedLetters
        assertEquals 4, gameState.moveCount
    }

    @Test
    void testStealingALetterWithMultiplePlaces() {
        IndividualGameState gameState = makeGameState("Elephantine", "Animal", 4)
        hangmanGameActions.guessLetter(gameState, (char) 'p')
        thievingHangmanGameActions.stealLetter(gameState, 2)
        assertFalse gameState.playerHung
        assertFalse gameState.puzzleSolved
        assertFalse gameState.puzzleOver
        assertEquals 1, gameState.penalties
        assertEquals 3, gameState.penaltiesRemaining
        assertEquals 1, gameState.featureData[GameFeature.ThievingCountTracking]
        assertEquals Arrays.asList(true, false, true, false, false, false, false, false, false, false, true), gameState.featureData[GameFeature.ThievingPositionTracking]
        assertEquals([(char) 'E'], gameState.featureData[GameFeature.ThievingLetters])
        assertEquals "E_EP______E", gameState.workingWordPhraseString
        assertEquals new TreeSet<>([(char) 'P']), gameState.guessedLetters
        assertEquals 2, gameState.moveCount
        assertEquals 7, gameState.blanksRemaining
    }

    @Test
    void testStealingToWin() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'o')
        thievingHangmanGameActions.stealLetter(gameState, 3)
        assertFalse gameState.playerHung
        assertTrue gameState.puzzleSolved
        assertTrue gameState.puzzleOver
        assertEquals 1, gameState.penalties
        assertEquals 2, gameState.penaltiesRemaining
        assertEquals 1, gameState.featureData[GameFeature.ThievingCountTracking]
        assertEquals Arrays.asList(false, false, false, true), gameState.featureData[GameFeature.ThievingPositionTracking]
        assertEquals([(char) 'G'], gameState.featureData[GameFeature.ThievingLetters])
        assertEquals "FROG", gameState.workingWordPhraseString
        assertEquals new TreeSet<>([(char) 'F', (char) 'R', (char) 'O']), gameState.guessedLetters
        assertTrue gameState.badlyGuessedLetters.empty
        assertEquals 4, gameState.moveCount
        assertEquals 0, gameState.blanksRemaining
    }


    @Test(expected = StealingKnownLetterException.class)
    void testExceptionOnStealingPreviouslyStolenLetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        thievingHangmanGameActions.stealLetter(gameState, 3)
        thievingHangmanGameActions.stealLetter(gameState, 3)
    }

    @Test(expected = StealingKnownLetterException.class)
    void testExceptionOnStealingPreviouslyGuessedLetter() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 3)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        thievingHangmanGameActions.stealLetter(gameState, 0)
    }

    @Test(expected = StealingOnFinalPenaltyException.class)
    void testExceptionOnStealingOnFinalPenalty() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        hangmanGameActions.guessLetter(gameState, (char) 'F')
        hangmanGameActions.guessLetter(gameState, (char) 'r')
        hangmanGameActions.guessLetter(gameState, (char) 'a')
        thievingHangmanGameActions.stealLetter(gameState, 3)
    }


    @Test(expected = StealingNegativePositionException.class)
    void testStealingNegativePosition() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        thievingHangmanGameActions.stealLetter(gameState, -1)
    }

    @Test(expected = StealingPositionBeyondEndException.class)
    void testStealingTooLongPosition() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 2)
        thievingHangmanGameActions.stealLetter(gameState, 4)
    }

    @Test(expected = GameOverException.class)
    void testStealingLostGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "x")
        assertTrue gameState.playerHung
        assertTrue gameState.puzzleOver
        thievingHangmanGameActions.stealLetter(gameState, 1)
    }


    @Test(expected = GameOverException.class)
    void testStealingWonGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "f")
        hangmanGameActions.guessLetter(gameState, (char) "r")
        hangmanGameActions.guessLetter(gameState, (char) "o")
        hangmanGameActions.guessLetter(gameState, (char) "g")
        assertTrue gameState.puzzleSolved
        assertTrue gameState.puzzleOver
        thievingHangmanGameActions.stealLetter(gameState, 1)
    }
}
