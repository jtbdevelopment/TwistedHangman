package com.jtbdevelopment.gamecore.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.gamecore.dao.PlayerRepository
import com.jtbdevelopment.gamecore.players.Player
import org.bson.types.ObjectId

/**
 * Date: 11/26/14
 * Time: 4:18 PM
 */
class FriendFinderTest extends TwistedHangmanTestCase {
    FriendFinder finder = new FriendFinder();

    void testSumOfSourceBasedFinders() {
        Player param = new Player(id: new ObjectId("12ab".padRight(24, "0")), source: "MANUAL")
        def f1 = [
                handlesSource: {
                    String it ->
                        true;
                },
                findFriends  : {
                    Player p ->
                        assert p.is(param)
                        return [(SourceBasedFriendFinder.FRIENDS_KEY): [PONE, PTWO] as Set, 'Y': ['Yes'] as Set]
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
                        return [(SourceBasedFriendFinder.FRIENDS_KEY): [PONE, PTHREE] as Set, 'X': [1, 2, 3] as Set]
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
                    ObjectId it ->
                        assert it == param.id
                        return param
                }
        ] as PlayerRepository
        def masked = ['x': 'y', '1': '2']
        finder.friendMasker = [
                maskFriends: {
                    Set<Player> friends ->
                        assert friends == [PONE, PTWO, PTHREE] as Set
                        return masked
                }
        ] as FriendMasker

        assert finder.findFriends(param.id) == [
                (SourceBasedFriendFinder.MASKED_FRIENDS_KEY): masked,
                'Y'                                         : ['Yes'] as Set,
                'X'                                         : [1, 2, 3] as Set
        ]
    }

    void testNoPlayerInRepository() {
        Player param = new Player(id: new ObjectId("12ab".padRight(24, "0")), source: "MANUAL")

        finder.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == param.id
                        return null
                }
        ] as PlayerRepository

        try {
            finder.findFriends(param.id)
            fail("should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }

    void testDisabledPlayer() {
        Player param = new Player(id: new ObjectId("12ab".padRight(24, "0")), source: "MANUAL")

        finder.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == param.id
                        return PINACTIVE1
                }
        ] as PlayerRepository

        try {
            finder.findFriends(param.id)
            fail("should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }
}
