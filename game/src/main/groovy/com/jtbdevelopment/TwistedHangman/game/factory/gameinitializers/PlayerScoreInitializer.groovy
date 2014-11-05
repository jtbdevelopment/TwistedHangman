package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 10:00 PM
 */
@Component
@CompileStatic
class PlayerScoreInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        game.players.each {
            String it ->
                if (!game.playerScores.containsKey(it)) {
                    game.playerScores[it] = 0
                }
        }
    }
}
