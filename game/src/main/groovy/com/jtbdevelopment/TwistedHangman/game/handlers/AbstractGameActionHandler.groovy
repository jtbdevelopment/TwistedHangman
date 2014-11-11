package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.players.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
abstract class AbstractGameActionHandler<T> extends AbstractHandler {
    @Autowired
    protected GameRepository gameRepository

    @Autowired
    protected GamePhaseTransitionEngine transitionEngine

    private static final Logger logger = LoggerFactory.getLogger(AbstractGameActionHandler.class)

    abstract protected Game handleActionInternal(final Player player, final Game game, T param);

    public Game handleAction(final String playerID, final String gameID, T param = null) {
        Player player = loadPlayer(playerID)
        Game game = loadGame(gameID)
        validatePlayerForGame(game, player)
        return transitionEngine.evaluateGamePhaseForGame(gameRepository.save(handleActionInternal(player, game, param)))
    }

    private static void validatePlayerForGame(final Game game, final Player player) {
        if (!game.players.contains(player)) {
            throw new PlayerNotPartOfGameException()
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
