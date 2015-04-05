package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameValidator
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 6:36 PM
 */
@Component
@CompileStatic
class ValidFeatureSetGameValidator implements GameValidator<Game> {
    public static final String ERROR = "System Error - Combination of features is not valid somehow."
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidFeatureSetGameValidator.class)

    @Override
    String errorMessage() {
        return ERROR
    }

    @Override
    boolean validateGame(final Game game) {
        Set<GameFeature> validatingFeatures = game.features.findAll { GameFeature feature -> feature.validate } as Set
        if (!GameFeature.ALLOWED_COMBINATIONS.contains(validatingFeatures)) {
            LOGGER.warn("Managed to create game with invalid features - " + validatingFeatures + "\n" + game)
            return false
        }
        return true
    }
}
