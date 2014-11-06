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
class TwoPlayerGameValidator implements GameValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoPlayerGameValidator.class)
    public static final String ERROR = "Game marked as two player with more than two players."

    @Override
    boolean validateGame(final Game game) {
        if (game.features.contains(GameFeature.TwoPlayersOnly)) {
            if (game.players.size() != 2) {
                LOGGER.warn("Managed to create two player game without 2 players. " + game)
                return false
            }
        } else {
            if (game.players.size() == 2) {
                LOGGER.warn("Managed to miss marking two player game with 2 players. " + game)
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
