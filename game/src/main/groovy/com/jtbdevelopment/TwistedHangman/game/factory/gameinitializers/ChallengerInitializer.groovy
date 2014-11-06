package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.game.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 6:54 AM
 */
@Component
@CompileStatic
class ChallengerInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        if (game.features.contains(GameFeature.SystemPuzzles)) {
            game.wordPhraseSetter = Player.SYSTEM_ID_ID
        } else if (game.features.contains(GameFeature.AlternatingPuzzleSetter)) {
            game.wordPhraseSetter = game.players[0]
        } else if (game.features.contains(GameFeature.TwoPlayer)) {
            game.wordPhraseSetter = null
        }
        if (game.wordPhraseSetter) {
            game.solverStates.remove(game.wordPhraseSetter)
        }
    }
}
