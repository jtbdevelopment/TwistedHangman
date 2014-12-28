package com.jtbdevelopment.TwistedHangman.players.friendfinder;

import com.jtbdevelopment.TwistedHangman.players.Player;

import java.util.Map;
import java.util.Set;

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
public interface SourceBasedFriendFinder {
    public static final String FRIENDS_KEY = "friends";
    public static final String MASKED_FRIENDS_KEY = "masked_friends";
    public static final String INVITABLE_FRIENDS_KEY = "invitable";
    public static final String NOT_FOUND_KEY = "notfound";

    public abstract boolean handlesSource(final String source);

    /*
        Return a set of data regarding friends
            At a minimu, the FRIENDS_KEY needs to be provided with a list of Players
     */
    public abstract Map<String, Set<Object>> findFriends(final Player player);
}
