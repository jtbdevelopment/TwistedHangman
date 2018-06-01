package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.mongo.players.MongoPlayerFactory
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import com.jtbdevelopment.games.players.SystemPlayer
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@Component
@CompileStatic
class TwistedHangmanSystemPlayerCreator {
    public static final String TH_DISPLAY_NAME = "TwistedHangman"
    public static final ObjectId TH_ID = new ObjectId("000000000000000000000000");
    public static MongoPlayer TH_PLAYER
    public static String TH_MD5

    private static Logger logger = LoggerFactory.getLogger(SystemPlayer.class)
    private final MongoPlayerRepository playerRepository
    private final MongoPlayerFactory playerFactory

    TwistedHangmanSystemPlayerCreator(
            final MongoPlayerRepository playerRepository,
            final MongoPlayerFactory playerFactory) {
        this.playerRepository = playerRepository
        this.playerFactory = playerFactory
    }

    @PostConstruct
    void loadOrCreateSystemPlayers() {
        logger.info('Checking for system player.')
        Optional<MongoPlayer> player = playerRepository.findById(TH_ID)
        if (!player.present) {
            logger.info("Making system id")
            MongoSystemPlayer systemPlayer = (MongoSystemPlayer) playerFactory.newSystemPlayer()
            systemPlayer.id = TH_ID
            systemPlayer.displayName = TH_DISPLAY_NAME
            systemPlayer.sourceId = TH_ID.toHexString()
            TH_PLAYER = playerRepository.save(systemPlayer)

        } else {
            TH_PLAYER = player.get()
        }
        TH_MD5 = TH_PLAYER.md5;
        logger.info("Completed")
    }
}
