package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.dictionary.DictionaryType
import com.jtbdevelopment.games.dictionary.Validator
import com.jtbdevelopment.games.state.GamePhase
import org.junit.Test
import org.mockito.Mockito

import static org.junit.Assert.assertSame


/**
 * Date: 11/10/14
 * Time: 7:04 AM
 */
class SetPuzzleHandlerTest extends TwistedHangmanTestCase {
    private Validator validator = Mockito.mock(Validator.class)
    private PhraseSetter phraseSetter = Mockito.mock(PhraseSetter.class)
    private SetPuzzleHandler handler = new SetPuzzleHandler(null, null, null, null, null, null, validator, phraseSetter)

    @Test
    void testMultiPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())

        assertSame(game, handler.handleActionInternal(
                PTHREE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        Mockito.verify(phraseSetter).setWordPhrase(poneState, wp, c)
        Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c)
    }


    @Test
    void testTwoPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        assert game.is(handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        Mockito.verify(phraseSetter, Mockito.never()).setWordPhrase(poneState, wp, c)
        Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c)
    }

    @Test(expected = PuzzlesAreAlreadySetException.class)
    void testMultiPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())

        assert game.is(handler.handleActionInternal(
                PTHREE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        Mockito.verify(phraseSetter).setWordPhrase(poneState, wp, c)
        Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c)

        poneState.setWordPhrase("SET".toCharArray())
        ptwoState.setWordPhrase("SET".toCharArray())
        handler.handleActionInternal(
                PTHREE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
    }


    @Test(expected = PuzzlesAreAlreadySetException.class)
    void testTwoPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())

        assert game.is(handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        Mockito.verify(phraseSetter, Mockito.never()).setWordPhrase(poneState, wp, c)
        Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c)

        ptwoState.setWordPhrase("SET".toCharArray())
        handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
    }


    @Test(expected = InvalidPuzzleWordsException.class)
    void testInvalidWordPhraseCategory() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(Arrays.asList("1", "2"))
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(Collections.emptyList())

        handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
    }


    @Test(expected = GameIsNotInSetupPhaseException.class)
    void testPuzzleNotInSetupPhase() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Playing)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]

        handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
    }


    @Test(expected = PuzzlesAreAlreadySetException.class)
    void testMultiPlayerInvalidPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
        Mockito.when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())
        Mockito.when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum)).thenReturn(new ArrayList<String>())

        handler.handleActionInternal(
                PONE,
                game,
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
    }
}
