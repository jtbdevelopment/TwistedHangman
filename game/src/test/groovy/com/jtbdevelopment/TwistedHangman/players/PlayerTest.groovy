package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import org.junit.Test

/**
 * Date: 11/9/14
 * Time: 3:44 PM
 */
class PlayerTest extends TwistedHangmanTestCase {
    @Test
    void testEquals() {
        assert PONE.equals(PONE)
        assertFalse PONE.equals(PTWO)
        assert PONE.equals(new Player(id: PONE.id))
        assertFalse PONE.equals("String")
        assertFalse PONE.equals(null)
    }

    @Test
    void testHashCode() {
        String SOMEID = "SOMEID"
        Player player = new Player(id: SOMEID)
        assert SOMEID.hashCode() == player.hashCode()
    }

    @Test
    void testToString() {
        assert new Player(
                id: "0x123",
                disabled: false,
                displayName: "BAYMAX",
                source: "BIG HERO 6").toString() == "Player{id='0x123', source='BIG HERO 6', displayName='BAYMAX', disabled=false}"
    }
}
