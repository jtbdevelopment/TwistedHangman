package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/5/14
 * Time: 7:11 PM
 */
class ChallengerInitializerTest extends TwistedHangmanTestCase {
    ChallengerInitializer initializer = new ChallengerInitializer()


    public void testSystemPuzzles() {
        def expectedSolvers = [PONE: null, PTHREE: null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == Player.SYSTEM_PLAYER
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerSystemPuzzles() {
        def expectedSolvers = [(PONE): null, (PTHREE): null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == Player.SYSTEM_PLAYER
        assert game.solverStates == expectedSolvers
    }


    public void testAlternatingChallenger() {
        def expectedSolvers = [(PTHREE): null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.solverStates = [(PONE): null, (PTHREE): null]
        game.players = [PONE, PTHREE]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == PONE
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerAlternatingChallenger() {
        def expectedSolvers = [(PTHREE): null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.features += GameFeature.TwoPlayer
        game.solverStates = [(PONE): null, (PTHREE): null]
        game.players = [(PONE), (PTHREE)]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == PONE
        assert game.solverStates == expectedSolvers
    }


    public void testTwoPlayerSimulataneous() {
        def expectedSolvers = [(PONE): null, (PTHREE): null]

        Game game = new Game()
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assertNull game.wordPhraseSetter
        assert game.solverStates == expectedSolvers
    }
}
