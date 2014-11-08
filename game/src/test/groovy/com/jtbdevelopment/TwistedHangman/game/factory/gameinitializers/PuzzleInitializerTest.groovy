package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/5/14
 * Time: 8:00 PM
 */
class PuzzleInitializerTest extends THGroovyTestCase {
    PuzzleInitializer initializer = new PuzzleInitializer()

    @Test
    public void testInitializesPuzzlesForAllWhenSystemPuzzler() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = Player.SYSTEM_PLAYER
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
        def players = [PONE, PTWO, PTHREE]
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
        def players = [PONE, PTWO, PTHREE]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = PTHREE
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size() - 1
        players.findAll { it != PTHREE }.each {
            assert game.solverStates.containsKey(it)
            assert game.solverStates[it] in IndividualGameState
            assert game.solverStates[it].features == features
        }
    }
}
