package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.*
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

import java.time.ZonedDateTime

/**
 * Date: 11/14/14
 * Time: 6:17 PM
 */
@Component
@CompileStatic
class GameMasker {
    MaskedGame maskGameForPlayer(final Game game, final Player player) {
        MaskedGame playerMaskedGame = new MaskedGame()

        playerMaskedGame.maskedForPlayerID = player.id
        playerMaskedGame.maskedForPlayerMD5 = player.md5
        copyUnmaskedData(game, playerMaskedGame)
        copyMaskedData(game, player, playerMaskedGame)

        playerMaskedGame
    }

    protected void copyMaskedData(final Game game, final Player player, final MaskedGame playerMaskedGame) {
        Map<String, Player> idmap = [:]
        game.players.each {
            Player p ->
                playerMaskedGame.players[p.md5] = p.displayName
                idmap[p.id] = p
        }
        playerMaskedGame.initiatingPlayer = idmap[game.initiatingPlayer].md5
        game.playerRunningScores.each {
            String p, Integer score ->
                playerMaskedGame.playerRunningScores[idmap[p].md5] = score
        }
        game.playerRoundScores.each {
            String p, Integer score ->
                playerMaskedGame.playerRoundScores[idmap[p].md5] = score
        }
        game.playerStates.each {
            String p, PlayerState state ->
                playerMaskedGame.playerStates[idmap[p].md5] = state
        }
        game.featureData.each {
            GameFeature feature, Object data ->
                playerMaskedGame.featureData[feature] =
                        (data in String && idmap.containsKey(data)) ?
                                idmap[(String) data].md5 :
                                data
        }
        game.solverStates.findAll {
            String p, IndividualGameState gameState ->
                playerMaskedGame.solverStates[idmap[p].md5] = maskGameState(player, game, idmap[p], gameState, idmap)
        }
        playerMaskedGame.wordPhraseSetter =
                game.wordPhraseSetter ?
                        game.wordPhraseSetter == Player.SYSTEM_PLAYER.id ?
                                Player.SYSTEM_PLAYER.md5 :
                                idmap[game.wordPhraseSetter].md5 : null
    }

    protected MaskedIndividualGameState maskGameState(
            final Player playerMaskingFor,
            final Game game,
            final Player gameStatePlayer,
            final IndividualGameState gameState,
            final Map<String, Player> idmap) {
        MaskedIndividualGameState masked = new MaskedIndividualGameState()

        if (game.gamePhase == GamePhase.RoundOver ||
                game.gamePhase == GamePhase.NextRoundStarted ||
                game.gamePhase == GamePhase.Quit ||
                playerMaskingFor == gameStatePlayer ||
                game.wordPhraseSetter == playerMaskingFor.id ||
                (game.wordPhraseSetter == null && playerMaskingFor != gameStatePlayer)
        ) {
            masked.workingWordPhrase = gameState.workingWordPhraseString
            masked.guessedLetters.addAll(gameState.guessedLetters)
            masked.badlyGuessedLetters.addAll(gameState.badlyGuessedLetters)
            gameState.featureData.each {
                GameFeature feature, Object data ->
                    masked.featureData[feature] =
                            (data in String && idmap.containsKey(data)) ?
                                    idmap[(String) data].md5 :
                                    data
            }
        } else {
            masked.workingWordPhrase = ""
        }
        if (game.gamePhase == GamePhase.RoundOver ||
                game.gamePhase == GamePhase.NextRoundStarted ||
                game.gamePhase == GamePhase.Quit ||
                game.wordPhraseSetter == playerMaskingFor.id ||
                (game.wordPhraseSetter == null && playerMaskingFor != gameStatePlayer)
        ) {
            masked.wordPhrase = gameState.wordPhraseString
        } else {
            masked.wordPhrase = ""
        }

        masked.category = gameState.category
        masked.features.addAll(gameState.features)
        masked.isPlayerHung = gameState.isPlayerHung()
        masked.isPuzzleSolved = gameState.isPuzzleSolved()
        masked.isPuzzleOver = gameState.isPuzzleOver()
        masked.moveCount = gameState.moveCount
        masked.maxPenalties = gameState.maxPenalties
        masked.penalties = gameState.penalties
        masked.penaltiesRemaining = gameState.penaltiesRemaining
        masked.blanksRemaining = gameState.blanksRemaining
        masked
    }

    protected void copyUnmaskedData(final Game game, final MaskedGame playerMaskedGame) {
        playerMaskedGame.completed = convertTime(game.completed)
        playerMaskedGame.created = convertTime(game.created)
        playerMaskedGame.declined = convertTime(game.declined)
        playerMaskedGame.lastUpdate = convertTime(game.lastUpdate)
        playerMaskedGame.rematched = convertTime(game.rematched)
        playerMaskedGame.features.addAll(game.features)
        playerMaskedGame.gamePhase = game.gamePhase
        playerMaskedGame.id = game.id
        playerMaskedGame.round = game.round
    }

    protected Long convertTime(final ZonedDateTime value) {
        value ? value.toEpochSecond() : null
    }
}
