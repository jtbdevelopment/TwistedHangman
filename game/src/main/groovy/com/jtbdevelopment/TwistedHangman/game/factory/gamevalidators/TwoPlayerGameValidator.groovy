package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:14 PM
 */
@Component
@CompileStatic
class TwoPlayerGameValidator implements GameValidator {
    @Override
    boolean validateGame(final Game game) {
        if (game.features.contains(GameFeature.TwoPlayersOnly)) {
            if (game.players.size() != 2) {
                return false
            }
        }
        return true
    }
}
