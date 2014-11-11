package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dictionary.Validator
import com.jtbdevelopment.TwistedHangman.exceptions.GameIsNotInSetupPhaseException
import com.jtbdevelopment.TwistedHangman.exceptions.InvalidPuzzleWordsException
import com.jtbdevelopment.TwistedHangman.exceptions.PuzzlesAreAlreadySetException
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 11/10/14
 * Time: 7:04 AM
 */
class SetPuzzleHandlerTest extends TwistedHangmanTestCase {
    SetPuzzleHandler handler = new SetPuzzleHandler()

    @Test
    public void testMultiPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = PTHREE
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        []
                }

        ] as Validator
        Set<IndividualGameState> gamesSeen = [] as Set
        handler.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String wordPhrase, String category ->
                        assert wordPhrase == wp
                        assert category == c
                        assert gameState.is(poneState) || gameState.is(ptwoState)
                        gameState.wordPhrase = wp.toCharArray()
                        gameState.category = c
                        gamesSeen.add(gameState)
                }
        ] as PhraseSetter

        assert game.is(handler.handleActionInternal(
                PTHREE,
                game,
                [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c]))
        assert gamesSeen.size() == 2
    }

    @Test
    public void testTwoPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        []
                }

        ] as Validator
        Set<IndividualGameState> gamesSeen = [] as Set
        handler.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String wordPhrase, String category ->
                        assert wordPhrase == wp
                        assert category == c
                        assert gameState.is(ptwoState)
                        gameState.wordPhrase = wp.toCharArray()
                        gameState.category = c
                        gamesSeen.add(gameState)
                }
        ] as PhraseSetter

        assert game.is(handler.handleActionInternal(
                PONE,
                game,
                [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c]))
        assert gamesSeen.size() == 1
    }

    @Test
    public void testMultiPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = PTHREE
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        []
                }

        ] as Validator
        Set<IndividualGameState> gamesSeen = [] as Set
        handler.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String wordPhrase, String category ->
                        assert wordPhrase == wp
                        assert category == c
                        assert gameState.is(poneState) || gameState.is(ptwoState)
                        gameState.wordPhrase = wp.toCharArray()
                        gameState.category = c
                        gamesSeen.add(gameState)
                }
        ] as PhraseSetter

        assert game.is(handler.handleActionInternal(
                PTHREE,
                game,
                [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c]))
        assert gamesSeen.size() == 2
        try {
            handler.handleActionInternal(
                    PTHREE,
                    game,
                    [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c])
            fail("should not get here")
        } catch (PuzzlesAreAlreadySetException e) {
            //
        }
    }

    @Test
    public void testTwoPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        []
                }

        ] as Validator
        Set<IndividualGameState> gamesSeen = [] as Set
        handler.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String wordPhrase, String category ->
                        assert wordPhrase == wp
                        assert category == c
                        assert gameState.is(ptwoState)
                        gameState.wordPhrase = wp.toCharArray()
                        gameState.category = c
                        gamesSeen.add(gameState)
                }
        ] as PhraseSetter

        assert game.is(handler.handleActionInternal(
                PONE,
                game,
                [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c]))
        assert gamesSeen.size() == 1
        try {
            handler.handleActionInternal(
                    PONE,
                    game,
                    [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c])
            fail("Should not get here")
        } catch (PuzzlesAreAlreadySetException e) {

        }
    }


    @Test
    public void testInvalidWordPhraseCategory() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        if (it == wp) {
                            return ["1", "2"]
                        } else {
                            return [""]
                        }

                }

        ] as Validator

        try {
            handler.handleActionInternal(
                    PONE,
                    game,
                    [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c])
            fail("Should not get here")
        } catch (InvalidPuzzleWordsException e) {
            assert e.message == "Your puzzle has invalid words [1, 2, ]."
        }
    }

    @Test
    public void testPuzzleNotInSetupPhase() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Playing)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        if (it == wp) {
                            return ["1", "2"]
                        } else {
                            return [""]
                        }

                }

        ] as Validator

        try {
            handler.handleActionInternal(
                    PONE,
                    game,
                    [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c])
            fail("Should not get here")
        } catch (GameIsNotInSetupPhaseException e) {

        }
    }

    @Test
    public void testMultiPlayerInvalidPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: Game.GamePhase.Setup)
        game.wordPhraseSetter = PTHREE
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE): poneState, (PTWO): ptwoState]
        handler.validator = [
                validateWordPhrase: {
                    String it ->
                        assert it == wp || it == c
                        []
                }

        ] as Validator

        try {
            handler.handleActionInternal(
                    PONE,
                    game,
                    [(SetPuzzleHandler.WORDPHRASE_KEY): wp, (SetPuzzleHandler.CATEGORY_KEY): c])
            fail("should have failed")
        } catch (PuzzlesAreAlreadySetException e) {

        }

    }
}
