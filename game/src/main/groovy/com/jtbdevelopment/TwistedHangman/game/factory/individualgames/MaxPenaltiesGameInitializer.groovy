package com.jtbdevelopment.TwistedHangman.game.factory.individualgames

import com.jtbdevelopment.TwistedHangman.game.factory.IndividualGamesInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/5/14
 * Time: 6:59 PM
 */
@Component
@CompileStatic
class MaxPenaltiesGameInitializer implements IndividualGamesInitializer {
    @Override
    void initializeIndividualGameStates(final Game game) {
        int penalties = IndividualGameState.BASE_PENALTIES +
                (game.features.contains(GameFeature.DrawFace) ? IndividualGameState.FACE_PENALTIES : 0) +
                (game.features.contains(GameFeature.DrawGallows) ? IndividualGameState.GALLOWS_PENALTIES : 0)
        game.solverStates.values().each { IndividualGameState gameState -> gameState.maxPenalties = penalties }
    }
}
