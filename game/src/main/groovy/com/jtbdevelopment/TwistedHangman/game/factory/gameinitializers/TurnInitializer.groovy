package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 7:12 AM
 */
@Component
@CompileStatic
class TurnInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        if (game.features.contains(GameFeature.TurnBased)) {
            game.featureData[GameFeature.TurnBased] = game.players[0]
        }
    }
}
