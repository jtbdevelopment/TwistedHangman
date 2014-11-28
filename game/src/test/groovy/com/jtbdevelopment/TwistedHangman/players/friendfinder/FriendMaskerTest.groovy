package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/26/14
 * Time: 9:00 PM
 */
class FriendMaskerTest extends TwistedHangmanTestCase {
    FriendMasker masker = new FriendMasker();

    void testMaskFriends() {
        assert masker.maskFriends([PONE, PTWO, PTHREE] as Set) == [
                (PONE.md5)  : PONE.displayName,
                (PTWO.md5)  : PTWO.displayName,
                (PTHREE.md5): PTHREE.displayName
        ]
    }

    void testSetsPlayerSalt() {
        String existingSalt = Player.ID_SALT

        masker.md5Salter = 'THISSALT'
        masker.applySalt()
        assert Player.ID_SALT == masker.md5Salter

        Player.ID_SALT = existingSalt
    }
}
