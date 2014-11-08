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
class ThreePlusGameValidator implements GameValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreePlusGameValidator.class)
    public static final String ERROR = "Game's 3+ player marker is wrong."

    @Override
    boolean validateGame(final Game game) {
        if (game.features.contains(GameFeature.ThreePlus)) {
            if (game.players.size() != 3) {
                LOGGER.warn("Managed to create 3+ game incorrectly. " + game)
                return false
            }
        } else {
            if (game.players.size() > 2) {
                LOGGER.warn("Managed to miss marking 3+ game. " + game)
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