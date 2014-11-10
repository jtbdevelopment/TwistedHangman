package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/9/2014
 * Time: 5:27 PM
 */
@CompileStatic
@Component
class ChallengeResponseHandler extends AbstractHandler {
    public Game acceptChallenge(final String gameId, final String playerId, final Game.PlayerChallengeState response) {
        Game game = loadGame(gameId)
        if (game.gamePhase == Game.GamePhase.Challenge) {
            Player player = loadPlayer(playerId)
            validatePlayerForGame(game, player)
            if (Game.PlayerChallengeState.Pending != game.playerStates[player]) {

            }
            game.playerStates[player] = response
            return transitionEngine.evaluateGamePhaseForGame(gameRepository.save(game))
        }
    }
}
