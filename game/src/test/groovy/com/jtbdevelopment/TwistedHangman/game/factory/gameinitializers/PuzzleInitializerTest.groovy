package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/5/14
 * Time: 8:00 PM
 */
class PuzzleInitializerTest extends TwistedHangmanTestCase {
    PuzzleInitializer initializer = new PuzzleInitializer()

    public void testOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }

    public void testInitializesPuzzlesForAllWhenSystemPuzzler() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = TwistedHangmanSystemPlayerCreator.TH_PLAYER.id
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size()
        players.each {
            assert game.solverStates.containsKey(it.id)
            assert game.solverStates[it.id] in IndividualGameState
            assert game.solverStates[it.id].features == features
        }
    }


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
            assert game.solverStates.containsKey(it.id)
            assert game.solverStates[it.id] in IndividualGameState
            assert game.solverStates[it.id].features == features
        }
    }


    public void testInitializesPuzzlesForPlayerSetter() {
        Game game = new Game()
        def players = [PONE, PTWO, PTHREE]
        def features = [GameFeature.DrawFace, GameFeature.ThievingCountTracking] as Set
        game.players = players
        game.features += features
        game.wordPhraseSetter = PTHREE.id
        game.features
        initializer.initializeGame(game)

        assert game.solverStates.size() == players.size() - 1
        players.findAll { it != PTHREE }.each {
            assert game.solverStates.containsKey(it.id)
            assert game.solverStates[it.id] in IndividualGameState
            assert game.solverStates[it.id].features == features
        }
    }
}
