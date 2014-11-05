package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:16 PM
 */
@Component
@CompileStatic
class PlayerFieldsGameValidator implements GameValidator {
    @Override
    boolean validateGame(final Game game) {
        if (!game.players.contains(game.initiatingPlayer)) {
            return false
        }

        Set<String> playerSet = game.players as Set
        if (playerSet != game.playerStates.keySet()) {
            return false
        }

        if (playerSet != game.playerScores.keySet()) {
            return false
        }

        if (game.features.contains(GameFeature.SystemPuzzles)) {
            if (game.solverStates.keySet() != playerSet) {
                return false
            }
        } else {
            Set<String> nonChallengerPlayers = (playerSet.toList()) as Set
            nonChallengerPlayers.remove(game.wordPhraseSetter)
            if (game.solverStates.keySet() != nonChallengerPlayers) {
                return false
            }
        }


        return true
    }
}
