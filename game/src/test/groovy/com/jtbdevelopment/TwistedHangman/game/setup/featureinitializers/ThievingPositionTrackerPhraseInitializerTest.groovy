package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 11/6/14
 * Time: 8:58 PM
 */
class ThievingPositionTrackerPhraseInitializerTest extends GroovyTestCase {
    ThievingPositionTrackerPhraseInitializer initializer = new ThievingPositionTrackerPhraseInitializer()


    void testIgnoresNonThieving() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.featureData[GameFeature.ThievingPositionTracking] = "X"
        initializer.initializeForPhrase(gameState)
        assert gameState.featureData[GameFeature.ThievingPositionTracking] == "X"
    }


    void testSetsThieving() {
        IndividualGameState gameState = new IndividualGameState([GameFeature.Thieving] as Set)
        gameState.featureData[GameFeature.ThievingPositionTracking] = "X"
        gameState.wordPhrase = "XYZ".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.featureData[GameFeature.ThievingPositionTracking] == [false, false, false]
    }
}
