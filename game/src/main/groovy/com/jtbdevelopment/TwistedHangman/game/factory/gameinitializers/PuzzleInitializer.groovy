package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
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

        game.players.findAll { String it -> game.wordPhraseSetter != it }.each {
            String it ->
                game.solverStates[it] = new IndividualGameState(game.features)
        }
    }
}
