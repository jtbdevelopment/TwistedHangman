package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import com.jtbdevelopment.gamecore.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerMasker
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
@CompileStatic
abstract class AbstractFriendFinder<ID extends Serializable> {
    @Autowired
    List<SourceBasedFriendFinder> friendFinders
    @Autowired
    AbstractPlayerRepository<ID> playerRepository
    @Autowired
    PlayerMasker friendMasker

    Map<String, Object> findFriends(final ID playerId) {
        Player<ID> player = playerRepository.findOne(playerId)
        if (player == null || player.disabled) {
            throw new FailedToFindPlayersException()
        }
        Map<String, Object> friends = [:]
        friendFinders.each {
            SourceBasedFriendFinder<ID> friendFinder ->
                if (friendFinder.handlesSource(player.source)) {
                    Map<String, Set<Object>> subFriends = friendFinder.findFriends(player)
                    subFriends.each {
                        String key, Set<Object> values ->
                            if (friends.containsKey(key)) {
                                ((Set<Object>) friends[key]).addAll(values)
                            } else {
                                friends[key] = values;
                            }
                    }
                }
        }
        Set<Player> playerFriends = (Set<Player>) friends.remove(SourceBasedFriendFinder.FRIENDS_KEY)
        if (playerFriends) {
            friends[SourceBasedFriendFinder.MASKED_FRIENDS_KEY] = friendMasker.maskFriends(playerFriends)
        }
        return friends
    }
}
