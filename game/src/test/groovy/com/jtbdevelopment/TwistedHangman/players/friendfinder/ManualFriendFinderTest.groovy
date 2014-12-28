package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/26/14
 * Time: 1:12 PM
 */
class ManualFriendFinderTest extends TwistedHangmanTestCase {
    ManualFriendFinder finder = new ManualFriendFinder()

    void testHandlesSource() {
        assert finder.handlesSource(ManualFriendFinder.MANUAL)
        assertFalse finder.handlesSource("Facebook")
    }

    void testFindFriends() {
        def playerA = makeSimplePlayer("a")
        Player pX = makeSimplePlayer("b")
        Player pY = makeSimplePlayer("c")
        Player pZ = makeSimplePlayer("d")
        def ps = [pX, pY, pZ, playerA]
        finder.playerRepository = [
                findBySourceAndDisabled: {
                    String source, boolean disabled ->
                        assert source == ManualFriendFinder.MANUAL
                        assertFalse disabled
                        return ps
                }
        ] as PlayerRepository

        assert finder.findFriends(playerA) == [(SourceBasedFriendFinder.FRIENDS_KEY): [pX, pY, pZ] as Set]
    }
}
