package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private static final Logger logger = LoggerFactory.getLogger(FriendMasker.class)

    @Value('${player.md5salt:SALT}')
    String md5Salter

    Map<String, String> maskFriends(final Set<Player> friends) {
        friends.collectEntries {
            Player p ->
                [p.md5, p.displayName]
        }
    }

    @PostConstruct
    void applySalt() {
        if (md5Salter == 'SALT') {
            logger.warn('---------------------------------------')
            logger.warn('---------------------------------------')
            logger.warn('---------------------------------------')
            logger.warn('player.md5salt is using default value!!')
            logger.warn('---------------------------------------')
            logger.warn('---------------------------------------')
            logger.warn('---------------------------------------')
        }
        Player.ID_SALT = md5Salter
    }
}
