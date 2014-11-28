package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 11/26/14
 * Time: 8:51 PM
 */
@Component
@CompileStatic
class FriendMasker {
    @Value('${player.md5salt:DEFAULTSALT}')
    String md5Salter

    Map<String, String> maskFriends(final Set<Player> friends) {
        friends.collectEntries {
            Player p ->
                [p.md5, p.displayName]
        }
    }

    @PostConstruct
    void applySalt() {
        Player.ID_SALT = md5Salter
    }
}
