package com.jtbdevelopment.TwistedHangman.game.factory.individualgames

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 9:51 PM
 */
class MaxPenaltiesGameInitializerTest extends GroovyTestCase {
    MaxPenaltiesGameInitializer initializer = new MaxPenaltiesGameInitializer()

    @Test
    public void testBaseGame() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.maxPenalties == IndividualGameState.BASE_PENALTIES
        }
    }

    @Test
    public void testDrawBoth() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawFace
        game.features += GameFeature.DrawGallows
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES + IndividualGameState.GALLOWS_PENALTIES)
        }
    }

    @Test
    public void testDrawGallows() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawGallows
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.GALLOWS_PENALTIES)
        }
    }

    @Test
    public void testDrawFace() {
        Game game = new Game()
        game.features += GameFeature.Thieving
        game.features += GameFeature.DrawFace
        game.solverStates = ["1": new IndividualGameState([] as Set), "2": new IndividualGameState([] as Set)]

        initializer.initializeIndividualGameStates(game)
        game.solverStates.values().each {
            assert it.maxPenalties == (IndividualGameState.BASE_PENALTIES + IndividualGameState.FACE_PENALTIES)
        }
    }
}
