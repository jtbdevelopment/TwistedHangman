package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import com.jtbdevelopment.games.players.SystemPlayer
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@Component
@CompileStatic
class TwistedHangmanSystemPlayer {
    public static final String TH_DISPLAY_NAME = "TwistedHangman"
    public static final ObjectId TH_ID = new ObjectId("000000000000000000000000");
    public static MongoSystemPlayer TH_PLAYER
    public static String TH_MD5

    private static Logger logger = LoggerFactory.getLogger(SystemPlayer.class)
    @Autowired
    AbstractPlayerRepository playerRepository

    @PostConstruct
    void loadOrCreateSystemPlayers() {
        logger.info('Checking for system player.')
        MongoSystemPlayer player = (MongoSystemPlayer) playerRepository.findOne(TH_ID)
        if (player == null) {
            logger.info("Making system id")
            player = new MongoSystemPlayer(
                    id: TH_ID,
                    displayName: TH_DISPLAY_NAME,
                    sourceId: TH_ID.toHexString())
            player = playerRepository.save(player)
        }
        TH_PLAYER = player
        TH_MD5 = player.md5
        logger.info("Completed")
    }
}
