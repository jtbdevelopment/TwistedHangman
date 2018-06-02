package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/5/2014
 * Time: 9:48 PM
 */
class ThievingPuzzleStateInitializerTest extends GroovyTestCase {
    ThievingPuzzleStateInitializer initializer = new ThievingPuzzleStateInitializer()


    public void testInitializedThievingVariables() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.featureData[GameFeature.ThievingCountTracking] == 0
            assert it.featureData[GameFeature.ThievingPositionTracking] == Collections.emptyList()
            assert it.featureData[GameFeature.ThievingLetters] == []
        }
    }


    public void testNoInitializedIfNoThievingVariables() {
        Game game = new Game()
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.featureData[GameFeature.ThievingCountTracking] == null
            assert it.featureData[GameFeature.ThievingPositionTracking] == null
        }
    }

    public void testOrder() {
        assert GameInitializer.LATE_ORDER == initializer.order
    }
}
