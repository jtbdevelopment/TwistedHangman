package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 7:11 PM
 */
class ChallengerInitializerTest extends GroovyTestCase {
    ChallengerInitializer initializer = new ChallengerInitializer()

    @Test
    public void testSystemPuzzles() {
        def expectedSolvers = ["XXX": null, "YYY": null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == Player.SYSTEM_ID_ID
        assert game.solverStates == expectedSolvers
    }

    @Test
    public void testTwoPlayerSystemPuzzles() {
        def expectedSolvers = ["XXX": null, "YYY": null]
        Game game = new Game()
        game.features += GameFeature.SystemPuzzles
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == Player.SYSTEM_ID_ID
        assert game.solverStates == expectedSolvers
    }

    @Test
    public void testAlternatingChallenger() {
        def expectedSolvers = ["YYY": null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.solverStates = ["XXX": null, "YYY": null]
        game.players = ["XXX", "YYYY"]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == "XXX"
        assert game.solverStates == expectedSolvers
    }

    @Test
    public void testTwoPlayerAlternatingChallenger() {
        def expectedSolvers = ["YYY": null]

        Game game = new Game()
        game.features += GameFeature.AlternatingPuzzleSetter
        game.features += GameFeature.TwoPlayer
        game.solverStates = ["XXX": null, "YYY": null]
        game.players = ["XXX", "YYYY"]
        initializer.initializeGame(game)

        assert game.wordPhraseSetter == "XXX"
        assert game.solverStates == expectedSolvers
    }

    @Test
    public void testTwoPlayerSimulateneous() {
        def expectedSolvers = ["XXX": null, "YYY": null]

        Game game = new Game()
        game.features += GameFeature.TwoPlayer
        game.solverStates = expectedSolvers
        initializer.initializeGame(game)

        assertNull game.wordPhraseSetter
        assert game.solverStates == expectedSolvers
    }
}
