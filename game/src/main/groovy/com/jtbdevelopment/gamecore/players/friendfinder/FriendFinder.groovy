package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.gamecore.dao.PlayerRepository
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
@CompileStatic
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
class FriendFinder {
    @Autowired
    List<SourceBasedFriendFinder> friendFinders
    @Autowired
    PlayerRepository playerRepository
    @Autowired
    FriendMasker friendMasker

    Map<String, Object> findFriends(final ObjectId playerId) {
        Player player = playerRepository.findOne(playerId)
        if (player == null || player.disabled) {
            throw new FailedToFindPlayersException()
        }
        Map<String, Object> friends = [:]
        friendFinders.each {
            SourceBasedFriendFinder friendFinder ->
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
