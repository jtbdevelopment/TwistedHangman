package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/5/2014
 * Time: 9:51 PM
 */
class MaxPenaltiesInitializerTest extends GroovyTestCase {
    MaxPenaltiesInitializer initializer = new MaxPenaltiesInitializer()

    public void testOrder() {
        assert GameInitializer.LATE_ORDER == initializer.order
    }

    public void testBaseGame() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.maxPenalties == IndividualGameState.BASE_PENALTIES
        }
    }


    public void testDrawBoth() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawFace
        game.features += GameFeature.DrawGallows
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES + IndividualGameState.GALLOWS_PENALTIES)
        }
    }


    public void testDrawGallows() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawGallows
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.GALLOWS_PENALTIES)
        }
    }


    public void testDrawFace() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawFace
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeGame(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES)
        }
    }
}
