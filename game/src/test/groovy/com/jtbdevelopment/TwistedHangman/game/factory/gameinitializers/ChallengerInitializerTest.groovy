package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator

/**
 * Date: 11/5/14
 * Time: 7:11 PM
 */
class ChallengerInitializerTest extends TwistedHangmanTestCase {
    ChallengerInitializer initializer = new ChallengerInitializer()


    public void testSystemPuzzles() {
        def expectedSolvers = [(PONE.id): null, (PTHREE.id): null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.id
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerSystemPuzzles() {
        def expectedSolvers = [(PONE.id): null, (PTHREE.id): null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.id
        assert game.solverStates == expectedSolvers
    }


    public void testAlternatingChallenger() {
        def expectedSolvers = [(PTHREE.id): null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.solverStates = [(PONE.id): null, (PTHREE.id): null]
        game.players = [PONE, PTHREE]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == PONE.id
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerAlternatingChallenger() {
        def expectedSolvers = [(PTHREE.id): null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.features += GameFeature.TwoPlayer
        game.solverStates = [(PONE.id): null, (PTHREE.id): null]
        game.players = [(PONE), (PTHREE)]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == PONE.id
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerSimulataneous() {
        def expectedSolvers = [(PONE.id): null, (PTHREE.id): null]

        Game game = new Game()
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assertNull game.wordPhraseSetter
        assert game.solverStates == expectedSolvers
    }
}
