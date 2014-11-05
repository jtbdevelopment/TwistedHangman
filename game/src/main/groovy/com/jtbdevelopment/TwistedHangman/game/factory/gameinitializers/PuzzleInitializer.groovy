package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
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
        int penalties = IndividualGameState.BASE_PENALTIES +
                (game.features.contains(GameFeature.DrawFace) ? IndividualGameState.FACE_PENALTIES : 0) +
                (game.features.contains(GameFeature.DrawGallows) ? IndividualGameState.GALLOWS_PENALTIES : 0)

        game.players.findAll { String it -> game.wordPhraseSetter != it }.each {
            String it ->
                game.solverStates[it] = new IndividualGameState(new char[0], new char[0], "", penalties, game.features)
        }
    }
}
