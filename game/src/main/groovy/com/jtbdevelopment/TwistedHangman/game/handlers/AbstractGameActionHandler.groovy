package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.OutOfGamesForTodayException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.players.PlayerGameTracker
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.publish.GamePublisher
import com.jtbdevelopment.games.rest.handlers.AbstractGameGetterHandler
import com.jtbdevelopment.games.state.masked.MaskedMultiPlayerGame
import com.jtbdevelopment.games.state.masked.MultiPlayerGameMasker
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
@CompileStatic
abstract class AbstractGameActionHandler<T> extends AbstractGameGetterHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGameActionHandler.class)

    @Autowired
    protected GamePhaseTransitionEngine transitionEngine
    @Autowired
    protected GamePublisher gamePublisher
    @Autowired
    protected PlayerGameTracker gameTracker
    @Autowired
    protected MultiPlayerGameMasker gameMasker

    abstract protected Game handleActionInternal(final Player<ObjectId> player, final Game game, final T param);

    protected boolean requiresEligibilityCheck(final T param) {
        return false
    }

    public MaskedMultiPlayerGame handleAction(final ObjectId playerID, final ObjectId gameID, T param = null) {
        Player<ObjectId> player = loadPlayer(playerID)
        Game game = (Game) loadGame(gameID)
        validatePlayerForGame(game, player)
        Game updatedGame = updateGameWithEligibilityWrapper(player, game, param)
        return gameMasker.maskGameForPlayer(
                (Game) gamePublisher.publish(updatedGame, player),
                player
        )
    }

    protected Game updateGameWithEligibilityWrapper(Player<ObjectId> player, Game game, T param) {
        Game updatedGame
        PlayerGameTracker.GameEligibilityResult eligibilityResult = null
        if (requiresEligibilityCheck(param)) {
            eligibilityResult = gameTracker.getGameEligibility(player)
            if (eligibilityResult.eligibility == PlayerGameTracker.GameEligibility.NoGamesAvailable) {
                throw new OutOfGamesForTodayException()
            }
        }
        try {
            updatedGame = updateGame(player, game, param)
        } catch (Exception e) {
            try {
                if (eligibilityResult) {
                    gameTracker.revertGameEligibility(eligibilityResult)
                }
            } catch (Exception e2) {
                //  TODO - notify
                logger.warn('Failed to revert players game eligibility ' + eligibilityResult, e2)
            }
            throw e
        }
        updatedGame
    }

    protected Game updateGame(Player<ObjectId> player, Game game, T param) {
        Game updatedGame
        updatedGame = (Game) gameRepository.save(
                transitionEngine.evaluateGame(
                        rotateTurnBasedGame(
                                handleActionInternal(player, game, param))));
        updatedGame
    }

    protected Game rotateTurnBasedGame(final Game game) {
        if (game.gamePhase == GamePhase.Playing && game.features.contains(GameFeature.TurnBased)) {
            rotateOnePlayer(game)

            if (game.featureData[GameFeature.TurnBased] == game.wordPhraseSetter) {
                rotateOnePlayer(game);
            }
        }
        game
    }

    protected static void rotateOnePlayer(final Game game) {
        int index = game.players.findIndexOf { Player<ObjectId> p -> p.id == game.featureData[GameFeature.TurnBased] } + 1
        if (index >= game.players.size()) {
            index = 0
        }
        game.featureData[GameFeature.TurnBased] = game.players[index].id
    }

}
