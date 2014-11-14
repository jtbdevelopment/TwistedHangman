package com.jtbdevelopment.TwistedHangman.game.factory.individualgames

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 11/5/2014
 * Time: 9:48 PM
 */
class InitializeThievingSubGamesTest extends GroovyTestCase {
    InitializeThievingSubGames initializer = new InitializeThievingSubGames()


    public void testInitializedThievingVariables() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.featureData[GameFeature.ThievingCountTracking] == 0
            assert it.featureData[GameFeature.ThievingPositionTracking] == new char[0]
        }
    }


    public void testNoInitializedIfNoThievingVariables() {
        Game game = new Game()
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.featureData[GameFeature.ThievingCountTracking] == null
            assert it.featureData[GameFeature.ThievingPositionTracking] == null
        }
    }
}
