package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.factory.GameInitializer
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:04 PM
 */
@Component
class ThievingPuzzleStateInitializer implements GameInitializer<Game> {
    void initializeGame(final Game game) {
        if (game.features.contains(GameFeature.Thieving)) {
            game.solverStates.values().each {
                IndividualGameState gameState ->
                    gameState.featureData[GameFeature.ThievingCountTracking] = 0
                    gameState.featureData[GameFeature.ThievingPositionTracking] = new boolean[0]
                    gameState.featureData[GameFeature.ThievingLetters] = []
            }
        }
    }

    int getOrder() {
        return LATE_ORDER
    }
}
