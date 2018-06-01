package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayerFactory
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import static org.mockito.Matchers.isA

/**
 * Date: 1/13/15
 * Time: 6:59 AM
 */
class TwistedHangmanSystemPlayerCreatorTest extends GroovyTestCase {
    private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class)
    private MongoPlayerFactory playerFactory = Mockito.mock(MongoPlayerFactory.class)
    private TwistedHangmanSystemPlayerCreator systemPlayer = new TwistedHangmanSystemPlayerCreator(playerRepository, playerFactory)

    void testLoadsSystemPlayerIfExists() {
        def md5 = 'XA135'
        def p = [
                getMd5: {
                    return md5
                }
        ] as MongoSystemPlayer
        Mockito.when(playerRepository.findById(TwistedHangmanSystemPlayerCreator.TH_ID)).thenReturn(Optional.of(p))

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
        Mockito.when(playerRepository.findById(TwistedHangmanSystemPlayerCreator.TH_ID)).thenReturn(Optional.empty())
        Mockito.when(playerRepository.save(isA(MongoSystemPlayer.class))).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocation) throws Throwable {
                MongoSystemPlayer save = invocation.arguments[0]
                assertFalse save.adminUser
                assertFalse save.disabled
                assert save.displayName == TwistedHangmanSystemPlayerCreator.TH_DISPLAY_NAME
                assert save.id == TwistedHangmanSystemPlayerCreator.TH_ID
                assert save.sourceId == TwistedHangmanSystemPlayerCreator.TH_ID.toHexString()
                return p
            }
        })
        Mockito.when(playerFactory.newSystemPlayer()).thenReturn(new MongoSystemPlayer())

        systemPlayer.loadOrCreateSystemPlayers()
    }
}
