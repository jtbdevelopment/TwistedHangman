package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:39 PM
 */
@CompileStatic
@Component
class MultiplePlayerInitializer implements GameInitializer<Game> {
    void initializeGame(final Game game) {
        if (game.players.size() > 2) {
            game.features.add(GameFeature.ThreePlus)
        }
    }

    int getOrder() {
        return EARLY_ORDER
    }
}
