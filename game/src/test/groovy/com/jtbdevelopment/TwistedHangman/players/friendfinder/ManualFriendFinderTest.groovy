package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/26/14
 * Time: 1:12 PM
 */
class ManualFriendFinderTest extends GroovyTestCase {
    ManualFriendFinder finder = new ManualFriendFinder()

    void testHandlesSource() {
        assert finder.handlesSource(ManualFriendFinder.MANUAL)
        assertFalse finder.handlesSource("Facebook")
    }

    void testFindFriends() {
        def playerA = new Player(id: "A")
        Player pX = new Player(id: "X")
        Player pY = new Player(id: "Y")
        Player pZ = new Player(id: "Z")
        def ps = [pX, pY, pZ, playerA]
        finder.playerRepository = [
                findBySourceAndDisabled: {
                    String source, boolean disabled ->
                        assert source == ManualFriendFinder.MANUAL
                        assertFalse disabled
                        return ps
                }
        ] as PlayerRepository

        assert finder.findFriends(playerA) == [pX, pY, pZ] as Set
    }
}
