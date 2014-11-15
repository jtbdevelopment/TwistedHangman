package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/14/14
 * Time: 6:17 PM
 */
@Component
@CompileStatic
class GameMasker {
    MaskedGame maskGameForPlayer(final Game game, final Player player) {
        MaskedGame playerMaskedGame = new MaskedGame()

        playerMaskedGame.maskedForPlayer = player
        playerMaskedGame.maskedForPlayerMD5 = player.md5Hex
        copyUnmaskedData(game, playerMaskedGame)
        copyMaskedData(game, player, playerMaskedGame)

        playerMaskedGame
    }

    protected void copyMaskedData(final Game game, final Player player, final MaskedGame playerMaskedGame) {
        Map<String, Player> idmap = [:]
        game.players.each {
            Player p ->
                playerMaskedGame.players[p.md5Hex] = p.displayName
                idmap[p.id] = p
        }
        playerMaskedGame.initiatingPlayer = idmap[game.initiatingPlayer].md5Hex
        game.playerScores.each {
            String p, Integer score ->
                playerMaskedGame.playerScores[idmap[p].md5Hex] = score
        }
        game.playerStates.each {
            String p, PlayerChallengeState state ->
                playerMaskedGame.playerStates[idmap[p].md5Hex] = state
        }
        game.featureData.each {
            GameFeature feature, Object data ->
                playerMaskedGame.featureData[feature] =
                        (data in String && idmap.containsKey(data)) ?
                                idmap[(String) data].md5Hex :
                                data
        }
        game.solverStates.findAll {
            String p, IndividualGameState gameState ->
                if (p == player.id || game.wordPhraseSetter == null || game.wordPhraseSetter == player.id) {
                    playerMaskedGame.solverStates[idmap[p].md5Hex] = maskGameState(gameState, idmap)
                }
        }
        playerMaskedGame.wordPhraseSetter =
                game.wordPhraseSetter ?
                        game.wordPhraseSetter == Player.SYSTEM_PLAYER.id ?
                                Player.SYSTEM_PLAYER.md5Hex :
                                idmap[game.wordPhraseSetter].md5Hex : null
    }

    protected MaskedIndividualGameState maskGameState(
            final IndividualGameState gameState, final Map<String, Player> idmap) {
        MaskedIndividualGameState masked = new MaskedIndividualGameState()
        masked.badlyGuessedLetters.addAll(gameState.badlyGuessedLetters)
        masked.category = gameState.category
        masked.features.addAll(gameState.features)
        masked.guessedLetters.addAll(gameState.guessedLetters)
        masked.isGameLost = gameState.isGameLost()
        masked.isGameWon = gameState.isGameWon()
        masked.isGameOver = gameState.isGameOver()
        masked.moveCount = gameState.moveCount
        masked.maxPenalties = gameState.maxPenalties
        masked.penalties = gameState.penalties
        masked.penaltiesRemaining = gameState.penaltiesRemaining
        masked.workingWordPhrase = gameState.workingWordPhraseString
        gameState.featureData.each {
            GameFeature feature, Object data ->
                masked.featureData[feature] =
                        (data in String && idmap.containsKey(data)) ?
                                idmap[(String) data].md5Hex :
                                data
        }
        masked
    }

    protected void copyUnmaskedData(final Game game, final MaskedGame playerMaskedGame) {
        playerMaskedGame.completed = game.completed
        playerMaskedGame.created = game.created
        playerMaskedGame.declined = game.declined
        playerMaskedGame.lastUpdate = game.lastUpdate
        playerMaskedGame.rematched = game.rematched
        playerMaskedGame.features.addAll(game.features)
        playerMaskedGame.gamePhase = game.gamePhase
        playerMaskedGame.id = game.id
    }
}
