package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayer
import com.jtbdevelopment.games.games.PlayerState
import com.jtbdevelopment.games.players.Player
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
    MaskedGame maskGameForPlayer(final Game game, final Player<ObjectId> player) {
        MaskedGame playerMaskedGame = new MaskedGame()

        playerMaskedGame.maskedForPlayerID = player.idAsString
        playerMaskedGame.maskedForPlayerMD5 = player.md5
        copyUnmaskedData(game, playerMaskedGame)
        copyMaskedData(game, player, playerMaskedGame)

        playerMaskedGame
    }

    protected static void copyMaskedData(
            final Game game, final Player<ObjectId> player, final MaskedGame playerMaskedGame) {
        Map<ObjectId, Player<ObjectId>> idmap = [:]
        game.players.each {
            Player<ObjectId> p ->
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
                        game.wordPhraseSetter == TwistedHangmanSystemPlayer.TH_PLAYER.id ?
                                TwistedHangmanSystemPlayer.TH_PLAYER.md5 :
                                idmap[game.wordPhraseSetter].md5 : null
    }

    protected static MaskedIndividualGameState maskGameState(
            final Player<ObjectId> playerMaskingFor,
            final Game game,
            final Player<ObjectId> gameStatePlayer,
            final IndividualGameState gameState,
            final Map<ObjectId, Player<ObjectId>> idmap) {
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
        playerMaskedGame.completedTimestamp = convertTime(game.completedTimestamp)
        playerMaskedGame.created = convertTime(game.created)
        playerMaskedGame.declinedTimestamp = convertTime(game.declinedTimestamp)
        playerMaskedGame.lastUpdate = convertTime(game.lastUpdate)
        playerMaskedGame.rematchTimestamp = convertTime(game.rematchTimestamp)
        playerMaskedGame.features.addAll(game.features)
        playerMaskedGame.gamePhase = game.gamePhase
        playerMaskedGame.id = game.id.toHexString()
        playerMaskedGame.round = game.round
    }

    protected static Long convertTime(final ZonedDateTime value) {
        value ? value.toInstant().toEpochMilli() : null
    }
}
