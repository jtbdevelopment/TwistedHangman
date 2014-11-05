package com.jtbdevelopment.TwistedHangman.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.Game
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
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
        return HangmanGameFeature.ALLOWED_COMBINATIONS.contains(game.features)
    }
}
