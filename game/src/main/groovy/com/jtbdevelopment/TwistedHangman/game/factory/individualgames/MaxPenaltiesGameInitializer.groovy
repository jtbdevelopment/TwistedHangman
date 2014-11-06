package com.jtbdevelopment.TwistedHangman.game.factory.individualgames

import com.jtbdevelopment.TwistedHangman.factory.IndividualGamesInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 11/5/14
 * Time: 6:59 PM
 */
class MaxPenaltiesGameInitializer implements IndividualGamesInitializer {
    @Override
    void initializeIndividualGameStates(final Game game) {
        int penalties = IndividualGameState.BASE_PENALTIES +
                (game.features.contains(GameFeature.DrawFace) ? IndividualGameState.FACE_PENALTIES : 0) +
                (game.features.contains(GameFeature.DrawGallows) ? IndividualGameState.GALLOWS_PENALTIES : 0)
        game.solverStates.values().each { IndividualGameState gameState -> gameState.maxPenalties = penalties }
    }
}
