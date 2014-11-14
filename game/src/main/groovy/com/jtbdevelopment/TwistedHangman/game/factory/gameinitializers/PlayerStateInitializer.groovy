package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.players.Player
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
        game.playerStates.put(game.initiatingPlayer.id, PlayerChallengeState.Accepted)
        game.players.findAll { Player it -> game.initiatingPlayer != it }.each {
            Player it ->
                game.playerStates[it.id] = PlayerChallengeState.Pending
        }
    }
}
