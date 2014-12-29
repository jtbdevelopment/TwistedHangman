package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Date: 11/26/14
 * Time: 8:51 PM
 */
@Component
@CompileStatic
class FriendMasker {
    private static final Logger logger = LoggerFactory.getLogger(FriendMasker.class)

    @SuppressWarnings("GrMethodMayBeStatic")
    Map<String, String> maskFriends(final Set<Player> friends) {
        friends.collectEntries {
            Player p ->
                [p.md5, p.displayName]
        }
    }
}
