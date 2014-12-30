package com.jtbdevelopment.gamecore.mongo.players.friendfinder

import com.jtbdevelopment.gamecore.players.friendfinder.AbstractManualFriendFinder
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 12/30/14
 * Time: 9:19 AM
 */
@Component
class ManualFriendFinder extends AbstractManualFriendFinder<ObjectId> {
}
