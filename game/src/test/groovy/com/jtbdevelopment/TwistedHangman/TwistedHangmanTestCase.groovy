package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayer
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import org.bson.types.ObjectId

/**
 * Date: 11/8/14
 * Time: 9:09 AM
 */
abstract class TwistedHangmanTestCase extends MongoGameCoreTestCase {
    {
        TwistedHangmanSystemPlayer.TH_PLAYER = new MongoSystemPlayer(
                id: TwistedHangmanSystemPlayer.TH_ID,
                displayName: TwistedHangmanSystemPlayer.TH_DISPLAY_NAME,
                sourceId: TwistedHangmanSystemPlayer.TH_ID.toHexString()
        )
        TwistedHangmanSystemPlayer.TH_MD5 = TwistedHangmanSystemPlayer.TH_PLAYER.md5
    }
    protected static Game makeSimpleGame(final String id) {
        return new Game(id: new ObjectId(id.padRight(24, "0")))
    }
}
