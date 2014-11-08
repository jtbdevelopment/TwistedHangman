package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/8/14
 * Time: 9:09 AM
 */
abstract class THGroovyTestCase extends GroovyTestCase {
    protected static final Player PONE = makeSimplePlayer("1")
    protected static final Player PTWO = makeSimplePlayer("2")
    protected static final Player PTHREE = makeSimplePlayer("3")
    protected static final Player PFOUR = makeSimplePlayer("4")

    protected static Player makeSimplePlayer(final String id) {
        return new Player(id: id, source: "MADEUP", displayName: id)
    }
}
