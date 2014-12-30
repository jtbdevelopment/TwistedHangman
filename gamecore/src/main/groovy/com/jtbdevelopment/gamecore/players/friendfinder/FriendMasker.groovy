package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/26/14
 * Time: 8:51 PM
 *
 * TODO - rename classes and packages
 */
@Component
@CompileStatic
class FriendMasker {
    @SuppressWarnings("GrMethodMayBeStatic")
    Map<String, String> maskFriends(final Set<? extends PlayerInt> friends) {
        friends.collectEntries {
            PlayerInt p ->
                [p.md5, p.displayName]
        }
    }
}
