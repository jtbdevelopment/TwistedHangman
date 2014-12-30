package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic

/**
 * Date: 12/30/2014
 * Time: 12:03 PM
 */
@CompileStatic
interface SourceBasedFriendFinder<ID extends Serializable> {
    static final String FRIENDS_KEY = "friends";
    static final String MASKED_FRIENDS_KEY = "masked_friends";
    static final String INVITABLE_FRIENDS_KEY = "invitable";
    static final String NOT_FOUND_KEY = "notfound";

    boolean handlesSource(final String source);

    /*
        Return a set of data regarding friends
            At a minimu, the FRIENDS_KEY needs to be provided with a list of Players
     */

    Map<String, Set<Object>> findFriends(final PlayerInt<ID> player);
}