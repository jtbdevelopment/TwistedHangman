package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/26/14
 * Time: 1:09 PM
 */
@Component
@CompileStatic
class ManualFriendFinder implements SourceBasedFriendFinder {
    public static final String MANUAL = "MANUAL"
    @Autowired
    PlayerRepository playerRepository

    @Override
    boolean handlesSource(final String source) {
        return MANUAL == source
    }

    @Override
    Map<String, Set<Object>> findFriends(final Player player) {
        Set<? extends Player> players = playerRepository.findBySourceAndDisabled(MANUAL, false) as Set
        players.remove(player)
        return [(FRIENDS_KEY): players]
    }
}
