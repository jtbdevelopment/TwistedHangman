package com.jtbdevelopment.TwistedHangman.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 6:39 PM
 */
@Component
@CompileStatic
class SingleWinnerInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        if (game.features.contains(GameFeature.SingleWinner)) {
            game.featureData[GameFeature.SingleWinner] = ""
        }
    }
}
