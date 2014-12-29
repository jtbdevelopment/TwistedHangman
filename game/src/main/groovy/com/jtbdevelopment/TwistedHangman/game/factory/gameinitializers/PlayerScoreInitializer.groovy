package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.gamecore.players.Player
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
            Player it ->
                game.playerRoundScores[it.id] = 0
                if (!game.playerRunningScores.containsKey(it.id)) {
                    game.playerRunningScores[it.id] = 0
                }
        }
    }
}
