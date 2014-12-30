package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
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
    protected AbstractPlayerRepository<ObjectId> playerRepository

    protected Set<PlayerInt<ObjectId>> loadPlayerMD5s(final Collection<String> playerMD5s) {
        LinkedHashSet<PlayerInt<ObjectId>> players = new LinkedHashSet<>(playerRepository.findByMd5In(playerMD5s).collect { PlayerInt it -> it })
        if (players.size() != playerMD5s.size()) {
            logger.info("Not all players were loaded " + playerMD5s + " vs. " + players)
            throw new FailedToFindPlayersException()
        }
        players
    }

    protected PlayerInt<ObjectId> loadPlayer(final ObjectId playerID) {
        PlayerInt<ObjectId> player = playerRepository.findOne(playerID)
        if (player == null) {
            logger.info("Player was not loaded " + playerID.toHexString())
            throw new FailedToFindPlayersException()
        }
        player
    }

}
