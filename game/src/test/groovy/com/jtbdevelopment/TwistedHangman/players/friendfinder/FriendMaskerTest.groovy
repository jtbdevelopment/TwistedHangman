package com.jtbdevelopment.TwistedHangman.players.friendfinder

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase

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
}
