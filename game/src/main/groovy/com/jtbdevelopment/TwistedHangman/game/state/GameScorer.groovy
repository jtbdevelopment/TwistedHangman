package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.gamecore.players.PlayerInt
import com.jtbdevelopment.gamecore.players.SystemPlayer
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/8/2014
 * Time: 4:59 PM
 */
@Component
@CompileStatic
class GameScorer {
    @SuppressWarnings("GrMethodMayBeStatic")
    public Game scoreGame(final Game game) {
        int winners = 0
        int losers = 0
        game.solverStates.each {
            ObjectId playerId, IndividualGameState gameState ->
                if (gameState.puzzleSolved) {
                    winners++
                    game.playerRoundScores[playerId] = 1
                    game.playerRunningScores[playerId] = game.playerRunningScores[playerId] + 1
                    //  Move to list of post-scorers?
                    if (game.features.contains(GameFeature.SingleWinner)) {
                        game.featureData[GameFeature.SingleWinner] = (game.players.find { PlayerInt<ObjectId> player -> player.id == playerId }).id
                    }
                }
        }
        game.solverStates.each {
            ObjectId id, IndividualGameState gameState ->
                if (gameState.playerHung) {
                    losers++
                    game.playerRoundScores[id] = -1
                    game.playerRunningScores[id] = game.playerRunningScores[id] - 1
                }
        }

        if (game.wordPhraseSetter != null && game.wordPhraseSetter != SystemPlayer.SYSTEM_PLAYER.id) {
            int net = losers - winners
            game.playerRoundScores[game.wordPhraseSetter] = net
            game.playerRunningScores[game.wordPhraseSetter] = game.playerRunningScores[game.wordPhraseSetter] + net
        }
        return game
    }
}
