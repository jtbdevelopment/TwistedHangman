package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerOutOfTurnException
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
@CompileStatic
abstract class AbstractGameActionHandler<T> extends AbstractHandler {
    @Autowired
    protected GameRepository gameRepository

    @Autowired
    protected GamePhaseTransitionEngine transitionEngine

    @Autowired
    protected GameMasker gameMasker

    private static final Logger logger = LoggerFactory.getLogger(AbstractGameActionHandler.class)

    abstract protected Game handleActionInternal(final Player player, final Game game, T param);

    public MaskedGame handleAction(final String playerID, final String gameID, T param = null) {
        Player player = loadPlayer(playerID)
        Game game = loadGame(gameID)
        validatePlayerForGame(game, player)
        return gameMasker.maskGameForPlayer(
                gameRepository.save(
                        transitionEngine.evaluateGamePhaseForGame(
                                rotateTurnBasedGame(
                                        handleActionInternal(player, game, param), player))),
                player)
    }

    private static Game rotateTurnBasedGame(final Game game, final Player player) {
        if (game.gamePhase == GamePhase.Playing && game.features.contains(GameFeature.TurnBased)) {
            int index = game.players.indexOf(player) + 1
            if (index >= game.players.size()) {
                index = 0
            }
            game.featureData[GameFeature.TurnBased] = game.players[index].id
        }
        game
    }

    private static void validatePlayerForGame(final Game game, final Player player) {
        if (!game.players.contains(player)) {
            throw new PlayerNotPartOfGameException()
        }
        if (game.features.contains(GameFeature.TurnBased)) {
            if (game.featureData[GameFeature.TurnBased] != player.id) {
                throw new PlayerOutOfTurnException()
            }
        }
    }

    private Game loadGame(final String gameID) {
        Game game = gameRepository.findOne(gameID)
        if (game == null) {
            logger.info("Game was not loaded " + gameID)
            throw new FailedToFindGameException()
        }
        game
    }
}
