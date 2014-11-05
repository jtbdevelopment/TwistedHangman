package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 7:01 AM
 */
@Component
@CompileStatic
class PlayerStateInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        game.playerStates.put(game.initiatingPlayer, Game.PlayerChallengeState.Accepted)
        game.players.findAll { String it -> game.initiatingPlayer != it }.each {
            String it ->
                game.playerStates[it] = Game.PlayerChallengeState.Pending
        }
    }
}
