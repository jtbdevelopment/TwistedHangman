package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/19/14
 * Time: 7:01 AM
 */
@CompileStatic
class AbstractGameGetterHandler extends AbstractHandler {
    @Autowired
    protected GameRepository gameRepository

    @Autowired
    protected GameMasker gameMasker

    private static final Logger logger = LoggerFactory.getLogger(AbstractGameActionHandler.class)

    protected void validatePlayerForGame(final Game game, final Player player) {
        if (!game.players.contains(player)) {
            throw new PlayerNotPartOfGameException()
        }
    }

    protected Game loadGame(final ObjectId gameID) {
        Game game = gameRepository.findOne(gameID)
        if (game == null) {
            logger.info("Game was not loaded " + gameID.toHexString())
            throw new FailedToFindGameException()
        }
        game
    }
}
