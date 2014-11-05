package com.jtbdevelopment.TwistedHangman.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.Game
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
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
        int penalties = HangmanGameState.BASE_PENALTIES +
                (game.features.contains(HangmanGameFeature.DrawFace) ? HangmanGameState.FACE_PENALTIES : 0) +
                (game.features.contains(HangmanGameFeature.DrawGallows) ? HangmanGameState.GALLOWS_PENALTIES : 0)

        game.players.findAll { String it -> game.wordPhraseSetter != it }.each {
            String it ->
                game.solverStates[it] = new HangmanGameState(new char[0], new char[0], "", penalties, game.features)
        }
    }
}
