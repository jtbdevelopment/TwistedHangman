package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.TwistedHangmanPlayerRepository
import com.jtbdevelopment.gamecore.players.Player

/**
 * Date: 11/26/14
 * Time: 1:12 PM
 */
class AbstractManualFriendFinderTest extends TwistedHangmanTestCase {
    AbstractManualFriendFinder finder = new AbstractManualFriendFinder()

    void testHandlesSource() {
        assert finder.handlesSource(AbstractManualFriendFinder.MANUAL)
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
                        assert source == AbstractManualFriendFinder.MANUAL
                        assertFalse disabled
                        return ps
                }
        ] as TwistedHangmanPlayerRepository

        assert finder.findFriends(playerA) == [(SourceBasedFriendFinder.FRIENDS_KEY): [pX, pY, pZ] as Set]
    }
}
