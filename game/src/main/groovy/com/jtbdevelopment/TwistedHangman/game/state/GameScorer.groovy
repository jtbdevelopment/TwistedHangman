package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/8/2014
 * Time: 4:59 PM
 */
@Component
@CompileStatic
class GameScorer {
    @Autowired
    GameRepository gameRepository

    public Game scoreGame(final Game game) {
        int winners = 0
        int losers = 0
        game.solverStates.each {
            String playerId, IndividualGameState gameState ->
                if (gameState.gameWon) {
                    winners++
                    game.playerScores[playerId] = game.playerScores[playerId] + 1
                    //  Move to list of post-scorers?
                    if (game.features.contains(GameFeature.SingleWinner)) {
                        game.featureData[GameFeature.SingleWinner] = game.players.find { Player player -> player.id == playerId }
                    }
                }
        }
        game.solverStates.each {
            String id, IndividualGameState gameState ->
                if (gameState.gameLost) {
                    losers++
                    game.playerScores[id] = game.playerScores[id] - 1
                }
        }

        if (game.wordPhraseSetter != null && game.wordPhraseSetter != Player.SYSTEM_PLAYER) {
            game.playerScores[game.wordPhraseSetter.id] = game.playerScores[game.wordPhraseSetter.id] + losers - winners
        }
        return gameRepository.save(game)
    }
}
