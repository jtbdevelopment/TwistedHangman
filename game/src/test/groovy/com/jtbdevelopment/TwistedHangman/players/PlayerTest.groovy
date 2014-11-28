package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase

/**
 * Date: 11/9/14
 * Time: 3:44 PM
 */
class PlayerTest extends TwistedHangmanTestCase {
    @Override
    void setUp() {
        Player.ID_SALT = 'SALTED'
    }

    void testEquals() {
        assert PONE.equals(PONE)
        assertFalse PONE.equals(PTWO)
        assert PONE.equals(new Player(id: PONE.id))
        assertFalse PONE.equals("String")
        assertFalse PONE.equals(null)
    }


    void testHashCode() {
        String SOMEID = "SOMEID"
        Player player = new Player(id: SOMEID)
        assert SOMEID.hashCode() == player.hashCode()
    }


    void testToString() {
        assert new Player(
                id: "0x123",
                disabled: false,
                displayName: "BAYMAX",
                source: "BIG HERO 6").toString() == "Player{id='0x123', source='BIG HERO 6', displayName='BAYMAX', disabled=false}"
    }

    void testMD5() {
        assert PONE.md5 == "0c244ca528410c6a625410af71982ba9"
    }
}
