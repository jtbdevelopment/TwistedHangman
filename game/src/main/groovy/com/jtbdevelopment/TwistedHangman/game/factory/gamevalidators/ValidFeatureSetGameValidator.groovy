package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 6:36 PM
 */
@Component
@CompileStatic
class ValidFeatureSetGameValidator implements GameValidator {
    @Override
    boolean validateGame(final Game game) {
        return GameFeature.ALLOWED_COMBINATIONS.contains(game.features)
    }
}
