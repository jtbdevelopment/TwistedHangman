package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameOverException
import com.jtbdevelopment.TwistedHangman.exceptions.input.LetterAlreadyGuessedException
import com.jtbdevelopment.TwistedHangman.exceptions.input.NotALetterGuessException
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */
class HangmanGameActionsTest extends AbstractGameActionsTest {
    private HangmanGameActions hangmanGameActions = new HangmanGameActions()

    @Override
    protected Set<GameFeature> getGameFeatures() {
        return new HashSet<GameFeature>()
    }

    @Test
    void testSingleLetterMatchGuess() {
        IndividualGameState gameState = makeGameState("Find A Letter", "Junk", 10)
        assert hangmanGameActions.guessLetter(gameState, (char) 'i') == 1
        assert !gameState.puzzleOver
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters == new TreeSet(['I'])
        assert gameState.workingWordPhraseString == "_I__ _ ______"
        assert gameState.category == "Junk"
        assert gameState.moveCount == 1
        assert gameState.penaltiesRemaining == 10
        assert gameState.blanksRemaining == 10
    }

    @Test
    void testMultipleLetterMatchGuess() {
        IndividualGameState gameState = makeGameState("Find A Letter", "Junk", 10)
        assert hangmanGameActions.guessLetter(gameState, (char) 'I') == 1
        assert hangmanGameActions.guessLetter(gameState, (char) 'e') == 2
        assert !gameState.puzzleOver
        assert gameState.penalties == 0
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters == new TreeSet(['E', 'I'])
        assert gameState.workingWordPhraseString == "_I__ _ _E__E_"
        assert gameState.moveCount == 2
        assert gameState.penaltiesRemaining == 10
        assert gameState.blanksRemaining == 8
    }

    @Test
    void testLetterMatchGuessFailure() {
        IndividualGameState gameState = makeGameState("Find A Letter", "JUNK", 10)
        assert hangmanGameActions.guessLetter(gameState, (char) 'I') == 1
        assert hangmanGameActions.guessLetter(gameState, (char) 'e') == 2
        assert hangmanGameActions.guessLetter(gameState, (char) 'Z') == 0
        assert hangmanGameActions.guessLetter(gameState, (char) 'X') == 0
        assert !gameState.puzzleOver
        assert gameState.penalties == 2
        assert gameState.badlyGuessedLetters == new TreeSet(['X', 'Z'])
        assert gameState.guessedLetters == new TreeSet(['E', 'I', 'X', 'Z'])
        assert gameState.workingWordPhraseString == "_I__ _ _E__E_"
        assert gameState.moveCount == 4
        assert gameState.penaltiesRemaining == 8
        assert gameState.blanksRemaining == 8
    }

    @Test
    void testGameLost() {
        IndividualGameState gameState = setUpAlmostFinishedGame()
        hangmanGameActions.guessLetter(gameState, (char) 'T')
        assert !gameState.puzzleSolved
        assert gameState.playerHung
        assert gameState.puzzleOver

        assert gameState.penalties == gameState.maxPenalties
        assert gameState.workingWordPhraseString == "WI__ER"
        assert gameState.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M', 'T'])
        assert gameState.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'T'])
        assert gameState.moveCount == 9
        assert gameState.penaltiesRemaining == 0
        assert gameState.blanksRemaining == 2
    }

    @Test
    void testGameWon() {
        IndividualGameState gameState = setUpAlmostFinishedGame()
        hangmanGameActions.guessLetter(gameState, (char) 'N')

        assert gameState.puzzleSolved
        assert !gameState.playerHung
        assert gameState.puzzleOver

        assert gameState.penalties < gameState.maxPenalties
        assert gameState.workingWordPhraseString == "WINNER"
        assert gameState.badlyGuessedLetters == new TreeSet(['G', 'X', 'L', 'M'])
        assert gameState.guessedLetters == new TreeSet(['W', 'I', 'E', 'R', 'G', 'X', 'L', 'M', 'N'])
        assert gameState.moveCount == 9
        assert gameState.penaltiesRemaining == 1
        assert gameState.blanksRemaining == 0
    }

    protected IndividualGameState setUpAlmostFinishedGame() {
        IndividualGameState gameState = makeGameState("Winner", "Not a Loser", 5)
        ['w', 'i', 'e', 'r'].each {
            String guess ->
                hangmanGameActions.guessLetter(gameState, guess.charAt(0))
                assert !gameState.isPuzzleSolved()
                assert !gameState.isPlayerHung()
                assert !gameState.isPuzzleOver()
        }
        ['g', 'x', 'l', 'm'].each {
            String guess ->
                hangmanGameActions.guessLetter(gameState, guess.charAt(0))
                assert !gameState.isPuzzleSolved()
                assert !gameState.isPuzzleSolved()
                assert !gameState.isPuzzleOver()
        }
        assert gameState.penalties == 4
        assert gameState.workingWordPhraseString == "WI__ER"
        gameState
    }

    @Test
    void testGuessingLostGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "x")
        assert gameState.playerHung
        assert gameState.puzzleOver
        try {
            hangmanGameActions.guessLetter(gameState, (char) "e")
            fail("Should not get here")
        } catch (GameOverException e) {
            assert GameOverException.GAME_OVER_ERROR == e.message
        }
    }

    @Test
    void testGuessingWonGame() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "f")
        hangmanGameActions.guessLetter(gameState, (char) "r")
        hangmanGameActions.guessLetter(gameState, (char) "o")
        hangmanGameActions.guessLetter(gameState, (char) "g")
        assert gameState.puzzleSolved
        assert gameState.puzzleOver
        try {
            hangmanGameActions.guessLetter(gameState, (char) "e")
            fail("Should not get here")
        } catch (GameOverException e) {
            assert GameOverException.GAME_OVER_ERROR == e.message
        }
    }

    @Test
    void testDuplicateGuess() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "f")
        try {
            hangmanGameActions.guessLetter(gameState, (char) "f")
            fail("Should not get here")
        } catch (LetterAlreadyGuessedException e) {
            assert LetterAlreadyGuessedException.ALREADY_GUESSED_ERROR == e.message
        }
    }

    @Test
    void testNonLetterGuess() {
        IndividualGameState gameState = makeGameState("Frog", "Animal", 1)
        hangmanGameActions.guessLetter(gameState, (char) "f")
        try {
            hangmanGameActions.guessLetter(gameState, (char) "1")
            fail("Should not get here")
        } catch (NotALetterGuessException e) {
            assert NotALetterGuessException.NOT_A_LETTER_ERROR == e.message
        }
    }
}

