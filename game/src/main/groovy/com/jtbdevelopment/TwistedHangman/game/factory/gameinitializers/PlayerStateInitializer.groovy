package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.PlayerState
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
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
        game.playerStates.put(game.initiatingPlayer, PlayerState.Accepted)
        game.players.findAll { Player<ObjectId> it -> game.initiatingPlayer != it.id }.each {
            Player<ObjectId> it ->
                game.playerStates[it.id] = PlayerState.Pending
        }
    }
}
