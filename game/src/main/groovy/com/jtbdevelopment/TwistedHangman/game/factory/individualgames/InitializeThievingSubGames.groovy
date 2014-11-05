package com.jtbdevelopment.TwistedHangman.game.factory.individualgames

import com.jtbdevelopment.TwistedHangman.factory.IndividualGamesInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:04 PM
 */
@Component
class InitializeThievingSubGames implements IndividualGamesInitializer {
    @Override
    void initializeIndividualGameStates(final Game game) {
        if (game.features.contains(GameFeature.Thieving)) {
            game.solverStates.values().each {
                IndividualGameState gameState ->
                    gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking] = 0
                    gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking] = new boolean[0]
            }
        }
    }
}