package com.jtbdevelopment.TwistedHangman.players.friendfinder;

import com.jtbdevelopment.TwistedHangman.players.Player;

import java.util.Set;

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
public interface SourceBasedFriendFinder {
    public abstract boolean handlesSource(final String source);

    public abstract Set<Player> findFriends(final Player player);
}
