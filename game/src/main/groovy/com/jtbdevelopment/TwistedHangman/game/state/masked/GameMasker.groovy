package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.*
import com.jtbdevelopment.gamecore.players.PlayerInt
import com.jtbdevelopment.gamecore.players.SystemPlayer
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

import java.time.ZonedDateTime

/**
 * Date: 11/14/14
 * Time: 6:17 PM
 */
@Component
@CompileStatic
class GameMasker {
    @SuppressWarnings("GrMethodMayBeStatic")
    MaskedGame maskGameForPlayer(final Game game, final PlayerInt<ObjectId> player) {
        MaskedGame playerMaskedGame = new MaskedGame()

        playerMaskedGame.maskedForPlayerID = player.idAsString
        playerMaskedGame.maskedForPlayerMD5 = player.md5
        copyUnmaskedData(game, playerMaskedGame)
        copyMaskedData(game, player, playerMaskedGame)

        playerMaskedGame
    }

    protected static void copyMaskedData(
            final Game game, final PlayerInt<ObjectId> player, final MaskedGame playerMaskedGame) {
        Map<ObjectId, PlayerInt<ObjectId>> idmap = [:]
        game.players.each {
            PlayerInt<ObjectId> p ->
                playerMaskedGame.players[p.md5] = p.displayName
                playerMaskedGame.playerImages[p.md5] = p.imageUrl
                playerMaskedGame.playerProfiles[p.md5] = p.profileUrl
                idmap[p.id] = p
        }
        playerMaskedGame.initiatingPlayer = idmap[game.initiatingPlayer].md5
        game.playerRunningScores.each {
            ObjectId p, Integer score ->
                playerMaskedGame.playerRunningScores[idmap[p].md5] = score
        }
        game.playerRoundScores.each {
            ObjectId p, Integer score ->
                playerMaskedGame.playerRoundScores[idmap[p].md5] = score
        }
        game.playerStates.each {
            ObjectId p, PlayerState state ->
                playerMaskedGame.playerStates[idmap[p].md5] = state
        }
        game.featureData.each {
            GameFeature feature, Object data ->
                playerMaskedGame.featureData[feature] =
                        (data in ObjectId && idmap.containsKey(data)) ?
                                idmap[(ObjectId) data].md5 :
                                data
        }
        game.solverStates.findAll {
            ObjectId p, IndividualGameState gameState ->
                playerMaskedGame.solverStates[idmap[p].md5] = maskGameState(player, game, idmap[p], gameState, idmap)
        }
        playerMaskedGame.wordPhraseSetter =
                game.wordPhraseSetter ?
                        game.wordPhraseSetter == SystemPlayer.SYSTEM_PLAYER.id ?
                                SystemPlayer.SYSTEM_PLAYER.md5 :
                                idmap[game.wordPhraseSetter].md5 : null
    }

    protected static MaskedIndividualGameState maskGameState(
            final PlayerInt<ObjectId> playerMaskingFor,
            final Game game,
            final PlayerInt<ObjectId> gameStatePlayer,
            final IndividualGameState gameState,
            final Map<ObjectId, PlayerInt<ObjectId>> idmap) {
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
                            (data in ObjectId && idmap.containsKey(data)) ?
                                    idmap[(ObjectId) data].md5 :
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

    protected static void copyUnmaskedData(final Game game, final MaskedGame playerMaskedGame) {
        playerMaskedGame.completed = convertTime(game.completed)
        playerMaskedGame.created = convertTime(game.created)
        playerMaskedGame.declined = convertTime(game.declined)
        playerMaskedGame.lastUpdate = convertTime(game.lastUpdate)
        playerMaskedGame.rematched = convertTime(game.rematched)
        playerMaskedGame.features.addAll(game.features)
        playerMaskedGame.gamePhase = game.gamePhase
        playerMaskedGame.id = game.id.toHexString()
        playerMaskedGame.round = game.round
    }

    protected static Long convertTime(final ZonedDateTime value) {
        value ? value.toInstant().toEpochMilli() : null
    }
}
