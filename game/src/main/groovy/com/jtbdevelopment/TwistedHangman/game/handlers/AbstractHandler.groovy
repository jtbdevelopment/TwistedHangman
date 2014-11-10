package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindPlayersException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/4/2014
 * Time: 9:54 PM
 */
@CompileStatic
abstract class AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHandler.class)

    @Autowired
    protected GameRepository gameRepository
    @Autowired
    protected PlayerRepository playerRepository
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine

    protected void validatePlayerForGame(final Game game, final Player player) {
        if (!game.players.contains(player)) {
            throw new PlayerNotPartOfGameException()
        }

    }

    protected LinkedHashSet<Player> loadPlayers(final Set<String> playerIDs) {
        LinkedHashSet<Player> players = new LinkedHashSet<>(playerRepository.findAll(playerIDs).collect { Player it -> it })
        if (players.size() != playerIDs.size()) {
            logger.info("Not all players were loaded " + playerIDs + " vs. " + players)
            throw new FailedToFindPlayersException()
        }
        players
    }

    protected Player loadPlayer(final String playerID) {
        Player player = playerRepository.findOne(playerID)
        if (player == null) {
            logger.info("Player was not loaded " + playerID)
            throw new FailedToFindPlayersException()
        }
        player
    }

    protected Game loadGame(final String gameID) {
        Game game = gameRepository.findOne(gameID)
        if (game == null) {
            logger.info("Game was not loaded " + gameID)
            throw new FailedToFindGameException()
        }
        game
    }
}
