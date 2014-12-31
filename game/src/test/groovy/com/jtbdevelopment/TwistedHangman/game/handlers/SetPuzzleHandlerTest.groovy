package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.dictionary.Validator

/**
 * Date: 11/10/14
 * Time: 7:04 AM
 */
class SetPuzzleHandlerTest extends TwistedHangmanTestCase {
    SetPuzzleHandler handler = new SetPuzzleHandler()


    public void testMultiPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        assert gamesSeen.size() == 2
    }


    public void testTwoPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        assert gamesSeen.size() == 1
    }


    public void testMultiPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        assert gamesSeen.size() == 2
        try {
            handler.handleActionInternal(
                    PTHREE,
                    game,
                    new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
            fail("should not get here")
        } catch (PuzzlesAreAlreadySetException e) {
            //
        }
    }


    public void testTwoPlayerSettingPuzzleSecondTimeExceptions() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp)))
        assert gamesSeen.size() == 1
        try {
            handler.handleActionInternal(
                    PONE,
                    game,
                    new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
            fail("Should not get here")
        } catch (PuzzlesAreAlreadySetException e) {

        }
    }


    public void testInvalidWordPhraseCategory() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                    new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
            fail("Should not get here")
        } catch (InvalidPuzzleWordsException e) {
            assert e.message == "Your puzzle has invalid words [1, 2, ]."
        }
    }


    public void testPuzzleNotInSetupPhase() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Playing)
        game.wordPhraseSetter = null
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                    new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
            fail("Should not get here")
        } catch (GameIsNotInSetupPhaseException e) {

        }
    }


    public void testMultiPlayerInvalidPlayerSettingPuzzle() {
        String wp = "A PUZZLE"
        String c = "CATEGORY"
        Game game = new Game(gamePhase: GamePhase.Setup)
        game.wordPhraseSetter = PTHREE.id
        IndividualGameState poneState = new IndividualGameState()
        IndividualGameState ptwoState = new IndividualGameState()
        game.solverStates = [(PONE.id): poneState, (PTWO.id): ptwoState]
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
                    new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: wp))
            fail("should have failed")
        } catch (PuzzlesAreAlreadySetException e) {

        }

    }
}
