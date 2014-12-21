package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
@CompileStatic
@Component
class FriendFinder {
    @Autowired
    List<SourceBasedFriendFinder> friendFinders
    @Autowired
    PlayerRepository playerRepository
    @Autowired
    FriendMasker friendMasker

    Map<String, String> findFriends(final ObjectId playerId) {
        Player player = playerRepository.findOne(playerId)
        if (player == null || player.disabled) {
            throw new FailedToFindPlayersException()
        }
        Set<? extends Player> friends = [] as Set;
        friendFinders.each {
            SourceBasedFriendFinder friendFinder ->
                if (friendFinder.handlesSource(player.source)) {
                    friends.addAll(friendFinder.findFriends(player))
                }
        }
        return friendMasker.maskFriends(friends);
    }
}
