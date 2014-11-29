package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/8/2014
 * Time: 4:59 PM
 */
@Component
@CompileStatic
class GameScorer {
    public Game scoreGame(final Game game) {
        int winners = 0
        int losers = 0
        game.solverStates.each {
            String playerId, IndividualGameState gameState ->
                if (gameState.puzzleSolved) {
                    winners++
                    game.playerRoundScores[playerId] = 1
                    game.playerRunningScores[playerId] = game.playerRunningScores[playerId] + 1
                    //  Move to list of post-scorers?
                    if (game.features.contains(GameFeature.SingleWinner)) {
                        game.featureData[GameFeature.SingleWinner] = (game.players.find { Player player -> player.id == playerId }).id
                    }
                }
        }
        game.solverStates.each {
            String id, IndividualGameState gameState ->
                if (gameState.playerHung) {
                    losers++
                    game.playerRoundScores[id] = -1
                    game.playerRunningScores[id] = game.playerRunningScores[id] - 1
                }
        }

        if (game.wordPhraseSetter != null && game.wordPhraseSetter != Player.SYSTEM_PLAYER.id) {
            int net = losers - winners
            game.playerRoundScores[game.wordPhraseSetter] = net
            game.playerRunningScores[game.wordPhraseSetter] = game.playerRunningScores[game.wordPhraseSetter] + net
        }
        return game
    }
}
