package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:14 PM
 */
@Component
@CompileStatic
class SinglePlayerGameValidator implements GameValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SinglePlayerGameValidator.class)
    public static final String ERROR = "Game marked as one player with more than one player."

    @Override
    boolean validateGame(final Game game) {
        if (game.features.contains(GameFeature.SinglePlayer)) {
            if (game.players.size() != 1) {
                LOGGER.warn("Managed to create single player game without single player. " + game)
                return false
            }
        } else {
            if (game.players.size() == 1) {
                LOGGER.warn("Managed to miss marking single player game. " + game)
                return false
            }
        }
        return true
    }

    @Override
    String errorMessage() {
        return ERROR
    }
}
