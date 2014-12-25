package com.jtbdevelopment.TwistedHangman.security.facebook

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.friendfinder.SourceBasedFriendFinder
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.PagedList
import org.springframework.social.facebook.api.Reference
import org.springframework.stereotype.Component

/**
 * Date: 12/20/2014
 * Time: 11:17 PM
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
@CompileStatic
class FacebookFriendFinder implements SourceBasedFriendFinder {
    @Autowired
    PlayerRepository playerRepository

    //  TODO - primarily for integration tests - code needs re-org
    @Autowired(required = false)
    Facebook facebook

    @Override
    boolean handlesSource(final String source) {
        return "facebook" == source
    }

    @Override
    Set<Player> findFriends(final Player player) {
        PagedList<Reference> friends = facebook.friendOperations().friends
        return friends.collect {
            Reference it ->
                playerRepository.findBySourceAndSourceId("facebook", it.id)
        }.findAll {
            Player p ->
                p != null
        } as Set
    }

    /*
    TODO
            friends = facebook.fetchConnections("me", "invitable_friends", Reference.class);
        friends.each {
            Reference it ->
                println it.id
                println it.name
                println it
        }

     */
}
