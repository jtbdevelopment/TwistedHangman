package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.dao.AbstractMultiPlayerGameRepository
import com.jtbdevelopment.games.exceptions.system.FailedToFindGameException
import com.jtbdevelopment.games.games.masked.MultiPlayerGameMasker
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/19/14
 * Time: 7:01 AM
 */
@CompileStatic
class AbstractGameGetterHandler<ID extends Serializable> extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGameGetterHandler.class)

    @Autowired
    protected AbstractMultiPlayerGameRepository gameRepository

    @Autowired
    protected MultiPlayerGameMasker gameMasker

    @SuppressWarnings("GrMethodMayBeStatic")
    protected void validatePlayerForGame(final Game game, final Player player) {
        if (!game.players.contains(player)) {
            throw new PlayerNotPartOfGameException()
        }
    }

    protected Game loadGame(final ID gameID) {
        Game game = (Game) gameRepository.findOne(gameID)
        if (game == null) {
            logger.info("Game was not loaded " + gameID)
            throw new FailedToFindGameException()
        }
        game
    }
}
