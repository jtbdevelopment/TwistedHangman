package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/26/14
 * Time: 4:18 PM
 */
class FriendFinderTest extends TwistedHangmanTestCase {
    FriendFinder finder = new FriendFinder();

    void testSumOfSourceBasedFinders() {
        Player param = new Player(id: "find", source: "MANUAL")
        def f1 = [
                handlesSource: {
                    String it ->
                        true;
                },
                findFriends  : {
                    Player p ->
                        assert p.is(param)
                        return [PONE, PTWO] as Set
                }
        ] as SourceBasedFriendFinder
        def f2 = [
                handlesSource: {
                    String it ->
                        true;
                },
                findFriends  : {
                    Player p ->
                        assert p.is(param)
                        return [PONE, PTHREE] as Set
                }
        ] as SourceBasedFriendFinder
        def f3 = [
                handlesSource: {
                    String it ->
                        false;
                }
        ] as SourceBasedFriendFinder

        finder.friendFinders = [f1, f2, f3]
        finder.playerRepository = [
                findOne: {
                    String it ->
                        assert it == param.id
                        return param
                }
        ] as PlayerRepository

        assert finder.findFriends(param.id) == [PONE, PTWO, PTHREE] as Set
    }
}
