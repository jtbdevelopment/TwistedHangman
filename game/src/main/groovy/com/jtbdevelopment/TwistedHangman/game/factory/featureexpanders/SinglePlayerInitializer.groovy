package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/6/14
 * Time: 6:48 AM
 */
@CompileStatic
@Component
class SinglePlayerInitializer implements GameInitializer<Game> {

    void initializeGame(final Game game) {
        if (game.players.size() == 1) {
            game.features.add(GameFeature.SinglePlayer)
            game.features.add(GameFeature.SingleWinner)
            game.features.add(GameFeature.SystemPuzzles)
        }
    }

    int getOrder() {
        return EARLY_ORDER
    }
}
