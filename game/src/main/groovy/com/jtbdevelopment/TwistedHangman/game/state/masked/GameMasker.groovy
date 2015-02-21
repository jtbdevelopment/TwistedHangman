package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.games.games.MultiPlayerGame
import com.jtbdevelopment.games.games.masked.MaskedMultiPlayerGame
import com.jtbdevelopment.games.mongo.games.masked.AbstractMongoMultiPlayerGameMasker
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
class GameMasker extends AbstractMongoMultiPlayerGameMasker<GameFeature, Game, MaskedGame> {
    @Override
    protected void copyMaskedData(
            final MultiPlayerGame<ObjectId, ZonedDateTime, GameFeature> g,
            final Player<ObjectId> player,
            final MaskedMultiPlayerGame<GameFeature> pmg, final Map<ObjectId, Player<ObjectId>> idMap) {
        super.copyMaskedData(g, player, pmg, idMap)
        Game game = (Game) g
        MaskedGame playerMaskedGame = (MaskedGame) pmg
        game.playerRunningScores.each {
            ObjectId p, Integer score ->
                playerMaskedGame.playerRunningScores[idMap[p].md5] = score
        }
        game.playerRoundScores.each {
            ObjectId p, Integer score ->
                playerMaskedGame.playerRoundScores[idMap[p].md5] = score
        }
        game.solverStates.findAll {
            ObjectId p, IndividualGameState gameState ->
                playerMaskedGame.solverStates[idMap[p].md5] = maskGameState(player, game, idMap[p], gameState, idMap)
        }
        playerMaskedGame.wordPhraseSetter =
                game.wordPhraseSetter ?
                        game.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.id ?
                                TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5 :
                                idMap[game.wordPhraseSetter].md5 : null
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    protected MaskedIndividualGameState maskGameState(
            final Player<ObjectId> playerMaskingFor,
            final Game game,
            final Player<ObjectId> gameStatePlayer,
            final IndividualGameState gameState,
            final Map<ObjectId, Player<ObjectId>> idMap) {
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
                            (data in ObjectId && idMap.containsKey(data)) ?
                                    idMap[(ObjectId) data].md5 :
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

    @Override
    protected void copyUnmaskedData(
            final MultiPlayerGame<ObjectId, ZonedDateTime, GameFeature> g,
            final MaskedMultiPlayerGame<GameFeature> pmg) {
        super.copyUnmaskedData(g, pmg)
        Game game = (Game) g
        MaskedGame playerMaskedGame = (MaskedGame) pmg
        playerMaskedGame.rematchTimestamp = convertTime(game.rematchTimestamp)
        playerMaskedGame.gamePhase = game.gamePhase
        playerMaskedGame.round = game.round
    }

    @Override
    protected MaskedGame newMaskedGame() {
        return new MaskedGame()
    }

}
