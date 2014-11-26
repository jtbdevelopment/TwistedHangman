package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/26/14
 * Time: 1:04 PM
 */
interface SourceBasedFriendFinder {
    boolean handlesSource(final String source);

    Set<Player> findFriends(final Player player);
}
