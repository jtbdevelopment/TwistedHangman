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
 * Time: 9:16 PM
 */
@Component
@CompileStatic
class PlayerFieldsGameValidator implements GameValidator {
    public final static String ERROR = "System error - players not setup properly."
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoPlayerGameValidator.class)

    @Override
    boolean validateGame(final Game game) {
        if (!game.players.contains(game.initiatingPlayer)) {
            LOGGER.warn("Game missing initiating player " + game)
            return false
        }

        Set<String> playerSet = game.players as Set
        if (playerSet != game.playerStates.keySet()) {
            LOGGER.warn("Game missing player states " + game)
            return false
        }

        if (playerSet != game.playerScores.keySet()) {
            LOGGER.warn("Game missing player scores " + game)
            return false
        }

        if (game.features.contains(GameFeature.SystemPuzzles)) {
            if (game.solverStates.keySet() != playerSet) {
                LOGGER.warn("Game missing player games " + game)
                return false
            }
        } else {
            Set<String> nonChallengerPlayers = (playerSet.toList()) as Set
            nonChallengerPlayers.remove(game.wordPhraseSetter)
            if (game.solverStates.keySet() != nonChallengerPlayers) {
                LOGGER.warn("Game missing player games " + game)
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
