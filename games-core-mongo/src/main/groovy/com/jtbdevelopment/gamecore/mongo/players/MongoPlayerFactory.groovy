package com.jtbdevelopment.gamecore.mongo.players

import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerFactory
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 12/30/2014
 * Time: 7:15 PM
 */
@Component
@CompileStatic
class MongoPlayerFactory implements PlayerFactory<ObjectId> {
    @Override
    Player<ObjectId> newPlayer() {
        return new MongoPlayer()
    }

    @Override
    Player<ObjectId> newManualPlayer() {
        return new MongoManualPlayer()
    }

    @Override
    Player<ObjectId> newSystemPlayer() {
        return new MongoSystemPlayer()
    }
}
