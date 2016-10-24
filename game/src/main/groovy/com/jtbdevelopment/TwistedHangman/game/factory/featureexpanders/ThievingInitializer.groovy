package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:27 PM
 */
@CompileStatic
@Component
class ThievingInitializer implements GameInitializer<Game> {

    void initializeGame(final Game game) {
        if (game.features.contains(GameFeature.Thieving)) {
            game.features.add(GameFeature.ThievingPositionTracking)
            game.features.add(GameFeature.ThievingCountTracking)
            game.features.add(GameFeature.ThievingLetters)
        }
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
