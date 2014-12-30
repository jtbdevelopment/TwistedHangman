package com.jtbdevelopment

import com.jtbdevelopment.gamecore.players.AbstractPlayer
import com.jtbdevelopment.gamecore.players.PlayerInt

/**
 * Date: 11/8/14
 * Time: 9:09 AM
 */
abstract class GameCoreTestCase extends GroovyTestCase {
    protected static final PlayerInt<String> PONE = makeSimplePlayer("1")
    protected static final PlayerInt<String> PTWO = makeSimplePlayer("2")
    protected static final PlayerInt<String> PTHREE = makeSimplePlayer("3")
    protected static final PlayerInt<String> PFOUR = makeSimplePlayer("4")
    protected static final PlayerInt<String> PFIVE = makeSimplePlayer("5")
    protected static final PlayerInt<String> PINACTIVE1 = makeSimplePlayer("A1", true)
    protected static final PlayerInt<String> PINACTIVE2 = makeSimplePlayer("A2", true)

    public static class StringPlayer extends AbstractPlayer<String> {
        String md5
        String id

        @Override
        protected void setMd5(final String md5) {
            this.md5 = md5;
        }

        @Override
        protected String getMd5Internal() {
            return this.md5
        }

        @Override
        String getIdAsString() {
            return id
        }
    }

    protected static PlayerInt<String> makeSimplePlayer(final String id, final boolean disabled = false) {
        return new StringPlayer(
                id: id,
                source: "MADEUP",
                sourceId: "MADEUP" + id,
                displayName: id,
                disabled: disabled,
                imageUrl: "http://somewhere.com/image/" + id,
                profileUrl: "http://somewhere.com/profile/" + id)
    }
}
