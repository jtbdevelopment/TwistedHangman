package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 7:03 AM
 */
@Component
@CompileStatic
class PuzzleInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {

        game.players.findAll { Player<ObjectId> it -> game.wordPhraseSetter != it.id }.each {
            Player<ObjectId> it ->
                game.solverStates[it.id] = new IndividualGameState(game.features)
        }
    }
}
