package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import com.jtbdevelopment.games.players.PlayerFactory
import org.bson.types.ObjectId

/**
 * Date: 1/13/15
 * Time: 6:59 AM
 */
class TwistedHangmanSystemPlayerCreatorTest extends GroovyTestCase {
    TwistedHangmanSystemPlayerCreator systemPlayer = new TwistedHangmanSystemPlayerCreator()

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
                        assert id == TwistedHangmanSystemPlayerCreator.TH_ID
                        return p
                }
        ] as AbstractPlayerRepository

        systemPlayer.loadOrCreateSystemPlayers()
        assert p.is(TwistedHangmanSystemPlayerCreator.TH_PLAYER)
        assert md5 == TwistedHangmanSystemPlayerCreator.TH_MD5
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
                        assert id == TwistedHangmanSystemPlayerCreator.TH_ID
                        return null
                },
                save   : {
                    MongoSystemPlayer save ->
                        assert save
                        assertFalse save.adminUser
                        assertFalse save.disabled
                        assert save.displayName == TwistedHangmanSystemPlayerCreator.TH_DISPLAY_NAME
                        assert save.id == TwistedHangmanSystemPlayerCreator.TH_ID
                        assert save.sourceId == TwistedHangmanSystemPlayerCreator.TH_ID.toHexString()
                        return p
                }
        ] as AbstractPlayerRepository
        systemPlayer.playerFactory = [
                newSystemPlayer: {
                    return new MongoSystemPlayer()
                }
        ] as PlayerFactory<ObjectId>

        systemPlayer.loadOrCreateSystemPlayers()
    }
}
