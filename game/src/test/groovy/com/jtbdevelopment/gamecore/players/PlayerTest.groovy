package com.jtbdevelopment.gamecore.players

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import org.bson.types.ObjectId

/**
 * Date: 11/9/14
 * Time: 3:44 PM
 */
class PlayerTest extends TwistedHangmanTestCase {
    void testEquals() {
        assert PONE.equals(PONE)
        assertFalse PONE.equals(PTWO)
        assert PONE.equals(new Player(id: PONE.id))
        assertFalse PONE.equals("String")
        assertFalse PONE.equals(null)
    }

    void testChangingSourceFails() {
        String originalSource = PONE.source
        PONE.source = originalSource + "XYZ"
        assert originalSource == PONE.source
    }

    void testHashCode() {
        def SOMEID = new ObjectId("1234".padRight(24, "0"))
        Player player = new Player(id: SOMEID)
        assert SOMEID.toHexString().hashCode() == player.hashCode()
    }


    void testToString() {
        assert new Player(
                id: new ObjectId("0a123".padRight(24, "0")),
                disabled: false,
                displayName: "BAYMAX",
                sourceId: "BAYMAX",
                source: "BIG HERO 6").toString() == "Player{id='0a1230000000000000000000', source='BIG HERO 6', sourceId='BAYMAX', displayName='BAYMAX', disabled=false}"
    }

    void testMD5() {
        assert PONE.md5 == "ee02ab36f4f4b92d0a2316022a11cce2"
    }

    void testMD5FromBlank() {
        Player p = new Player()
        assert p.md5 == ""
    }
}
