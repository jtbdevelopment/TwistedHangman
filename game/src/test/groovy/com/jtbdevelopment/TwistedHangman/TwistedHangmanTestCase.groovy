package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.gamecore.players.Player
import org.bson.types.ObjectId

/**
 * Date: 11/8/14
 * Time: 9:09 AM
 */
abstract class TwistedHangmanTestCase extends GroovyTestCase {
    protected static final Player PONE = makeSimplePlayer("1")
    protected static final Player PTWO = makeSimplePlayer("2")
    protected static final Player PTHREE = makeSimplePlayer("3")
    protected static final Player PFOUR = makeSimplePlayer("4")
    protected static final Player PFIVE = makeSimplePlayer("5")
    protected static final Player PINACTIVE1 = makeSimplePlayer("A1", true)
    protected static final Player PINACTIVE2 = makeSimplePlayer("A2", true)

    protected static Player makeSimplePlayer(final String id, final boolean disabled = false) {
        return new Player(
                id: new ObjectId(id.padRight(24, "0")),
                source: "MADEUP",
                sourceId: "MADEUP" + id,
                displayName: id,
                disabled: disabled,
                imageUrl: "http://somewhere.com/image/" + id,
                profileUrl: "http://somewhere.com/profile/" + id)
    }

    protected static Game makeSimpleGame(final String id) {
        return new Game(id: new ObjectId(id.padRight(24, "0")))
    }
}
