package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/5/14
 * Time: 6:59 PM
 */
@Component
@CompileStatic
class MaxPenaltiesInitializer implements GameInitializer<Game> {
    void initializeGame(final Game game) {
        int penalties = IndividualGameState.BASE_PENALTIES +
                (game.features.contains(GameFeature.DrawFace) ? IndividualGameState.FACE_PENALTIES : 0) +
                (game.features.contains(GameFeature.DrawGallows) ? IndividualGameState.GALLOWS_PENALTIES : 0)
        game.solverStates.values().each { IndividualGameState gameState -> gameState.maxPenalties = penalties }
    }

    int getOrder() {
        return LATE_ORDER
    }
}
