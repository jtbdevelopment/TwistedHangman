package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import org.bson.types.ObjectId

/**
 * Date: 1/13/15
 * Time: 6:59 AM
 */
class TwistedHangmanSystemPlayerTest extends GroovyTestCase {
    TwistedHangmanSystemPlayer systemPlayer = new TwistedHangmanSystemPlayer()

    void testLoadsSystemPlayerIfExists() {
        def md5 = 'XA135'
        def p = [
                getMd5: {
                    return md5
                }
        ] as MongoSystemPlayer
        systemPlayer.playerRepository = [
                findOne: {
                    ObjectId id ->
                        assert id == TwistedHangmanSystemPlayer.TH_ID
                        return p
                }
        ] as AbstractPlayerRepository

        systemPlayer.loadOrCreateSystemPlayers()
        assert p.is(TwistedHangmanSystemPlayer.TH_PLAYER)
        assert md5 == TwistedHangmanSystemPlayer.TH_MD5
    }

    void testCreatesIfMissing() {
        def md5 = 'XA135'
        def p = [
                getMd5: {
                    return md5
                }
        ] as MongoSystemPlayer
        systemPlayer.playerRepository = [
                findOne: {
                    ObjectId id ->
                        assert id == TwistedHangmanSystemPlayer.TH_ID
                        return null
                },
                save   : {
                    MongoSystemPlayer save ->
                        assert save
                        assertFalse save.adminUser
                        assertFalse save.disabled
                        assert save.displayName == TwistedHangmanSystemPlayer.TH_DISPLAY_NAME
                        assert save.sourceId == TwistedHangmanSystemPlayer.TH_ID.toHexString()
                        return p
                }
        ] as AbstractPlayerRepository

        systemPlayer.loadOrCreateSystemPlayers()
    }
}
