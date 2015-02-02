package com.jtbdevelopment.TwistedHangman

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import org.bson.types.ObjectId

/**
 * Date: 11/8/14
 * Time: 9:09 AM
 */
abstract class TwistedHangmanTestCase extends MongoGameCoreTestCase {
    {
        TwistedHangmanSystemPlayerCreator.TH_PLAYER = new MongoSystemPlayer(
                id: TwistedHangmanSystemPlayerCreator.TH_ID,
                displayName: TwistedHangmanSystemPlayerCreator.TH_DISPLAY_NAME,
                sourceId: TwistedHangmanSystemPlayerCreator.TH_ID.toHexString()
        )
        TwistedHangmanSystemPlayerCreator.TH_MD5 = TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5
    }
    protected static Game makeSimpleGame(final String id) {
        return new Game(id: new ObjectId(id.padRight(24, "0")))
    }
}
