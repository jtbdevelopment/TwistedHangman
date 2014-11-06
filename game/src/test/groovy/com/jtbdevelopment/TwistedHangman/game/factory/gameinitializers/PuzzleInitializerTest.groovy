package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 8:00 PM
 */
class PuzzleInitializerTest extends GroovyTestCase {
    PuzzleInitializer initializer = new PuzzleInitializer()

    @Test
    public void testInitializesPuzzlesForAllWhenSystemPuzzler() {
        Game game = new Game()
        def players = ["1", "2", "3"]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = Player.SYSTEM_ID_ID
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size()
        players.each {
            assert game.solverStates.containsKey(it)
            assert game.solverStates[it] in IndividualGameState
            assert game.solverStates[it].features == features
        }
    }

    @Test
    public void testInitializesPuzzlesForTwoPlayerSimultaneous() {
        Game game = new Game()
        def players = ["1", "2", "3"]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = null
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size()
        players.each {
            assert game.solverStates.containsKey(it)
            assert game.solverStates[it] in IndividualGameState
            assert game.solverStates[it].features == features
        }
    }

    @Test
    public void testInitializesPuzzlesForPlayerSetter() {
        Game game = new Game()
        def players = ["1", "2", "3"]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = "3"
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size() - 1
        players.findAll { it != "3" }.each {
            assert game.solverStates.containsKey(it)
            assert game.solverStates[it] in IndividualGameState
            assert game.solverStates[it].features == features
        }
    }
}
