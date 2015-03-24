package com.jtbdevelopment.TwistedHangman.players.reset

import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.publish.PlayerPublisher
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

import static com.jtbdevelopment.games.dao.caching.CacheConstants.*

/**
 * Date: 2/11/15
 * Time: 7:10 PM
 */
@Component
@CompileStatic
class PlayerFreeGameReset {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFreeGameReset.class)
    @Autowired
    MongoOperations mongoOperations

    @Autowired
    PlayerPublisher playerPublisher

    @Caching(
            evict = [
                    @CacheEvict(value = PLAYER_ID_CACHE, allEntries = true),
                    @CacheEvict(value = PLAYER_MD5_CACHE, allEntries = true),
                    @CacheEvict(value = PLAYER_S_AND_SID_CACHE, allEntries = true)
            ]
    )
    boolean resetFreeGames() {
        logger.info('Resetting all player free games.')
        //  Error check?
        mongoOperations.updateMulti(
                Query.query(Criteria.where(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD).gt(0)),
                Update.update(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD, 0),
                MongoPlayer.class
        )
        playerPublisher.publishAll()
    }
}
