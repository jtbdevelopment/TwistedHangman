package com.jtbdevelopment.TwistedHangman.players.reset

import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.TwistedHangman.publish.PlayerPublisher
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

/**
 * Date: 2/11/15
 * Time: 7:10 PM
 */
@Component
@CompileStatic
class PlayerFreeGameReset {
    @Autowired
    MongoOperations mongoOperations

    @Autowired
    PlayerPublisher playerPublisher

    boolean resetFreeGames() {
        //  Error check?
        mongoOperations.updateMulti(
                Query.query(Criteria.where(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD).gt(0)),
                Update.update(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD, 0),
                MongoPlayer.class
        )
        playerPublisher.publishAll()
    }
}
