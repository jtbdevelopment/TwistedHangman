package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.games.masked.MultiPlayerGameMasker
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory

/**
 * Date: 12/22/14
 * Time: 7:18 PM
 */
class AtmosphereListenerTest extends TwistedHangmanTestCase {

    AtmosphereListener listener = new AtmosphereListener()

    //  Initiating Server Flag irrelevant here
    boolean initiatingServer = new Random().nextBoolean()

    void testPublishPlayerToConnectedPlayer() {
        boolean p2pub = false;
        boolean p4pub = false;
        Broadcaster b2 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Player
                        assert o.player.is(PTWO)
                        assert o.message == null
                        p2pub = true
                        null
                }
        ] as Broadcaster
        Broadcaster b4 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Player
                        assert o.player.is(PFOUR)
                        assert o.message == null
                        p4pub = true
                        null
                }
        ] as Broadcaster
        BroadcasterFactory factory = [
                lookup: {
                    String id ->
                        switch (id) {
                            case LiveFeedService.PATH_ROOT + PONE.id.toHexString():
                                return null
                                break;
                            case LiveFeedService.PATH_ROOT + PTWO.id.toHexString():
                                return b2
                                break;
                            case LiveFeedService.PATH_ROOT + PTHREE.id.toHexString():
                                return null
                                break;
                            case LiveFeedService.PATH_ROOT + PFOUR.id.toHexString():
                                return b4
                                break;
                        }
                        fail("Not sure how we got here")
                }
        ] as BroadcasterFactory
        listener.broadcasterFactory = factory
        [PONE, PTWO, PTHREE, PFOUR].each {
            listener.playerChanged(it, initiatingServer)
        }
        assert p2pub && p4pub
    }

    void testPublishRefreshPlayerToAllValidConnectedPlayers() {
        boolean p2pub = false;
        boolean p4pub = false;
        Broadcaster bJunk = [
                broadcast: {
                    Object o ->
                        fail('should not get here')
                },
                getID    : {
                    return '/livefeed/JUNK'
                }
        ] as Broadcaster
        Broadcaster b2 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Player
                        assert o.player.is(PTWO)
                        assertNull o.message
                        assertNull o.game
                        p2pub = true
                        null
                },
                getID    : {
                    return '/livefeed/' + PTWO.idAsString
                }
        ] as Broadcaster
        Broadcaster b4 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Player
                        assert o.player.is(PFOUR)
                        assertNull o.message
                        assertNull o.game
                        p4pub = true
                        null
                },
                getID    : {
                    return '/livefeed/' + PFOUR.idAsString
                }
        ] as Broadcaster
        listener.broadcasterFactory = [
                lookupAll: {
                    return [b2, b4, bJunk]
                }
        ] as BroadcasterFactory
        listener.playerRepository = [
                findOne: {
                    String id ->
                        if (id == PTWO.idAsString) {
                            return PTWO
                        }
                        if (id == PFOUR.idAsString) {
                            return PFOUR
                        }
                        if (id == 'JUNK') {
                            return null
                        }
                        fail('unknown id')
                }
        ] as AbstractPlayerRepository
        listener.allPlayersChanged(initiatingServer)
        assert p2pub && p4pub
    }

    void testPublishGameToConnectedNonInitiatingPlayers() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        boolean p2pub = false;
        boolean p4pub = false;
        MaskedGame mg2 = new MaskedGame()
        MaskedGame mg4 = new MaskedGame()
        Broadcaster b2 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Game
                        assert o.game.is(mg2)
                        assert o.message == null
                        assert o.player == null
                        p2pub = true
                        null
                }
        ] as Broadcaster
        Broadcaster b4 = [
                broadcast: {
                    Object o ->
                        assert o in WebSocketMessage
                        assert o.messageType == WebSocketMessage.MessageType.Game
                        assert o.game.is(mg4)
                        assert o.message == null
                        assert o.player == null
                        p4pub = true
                        null
                }
        ] as Broadcaster
        MultiPlayerGameMasker masker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert game.is(g)
                        if (p == PTWO) return mg2
                        if (p == PFOUR) return mg4
                        fail("Masking for unexpected player")
                }
        ] as MultiPlayerGameMasker
        BroadcasterFactory factory = [
                lookup: {
                    String id ->
                        switch (id) {
                            case LiveFeedService.PATH_ROOT + PONE.id.toHexString():
                                fail("Should not be requesting PONE lookup")
                                break;
                            case LiveFeedService.PATH_ROOT + PTWO.id.toHexString():
                                return b2
                                break;
                            case LiveFeedService.PATH_ROOT + PTHREE.id.toHexString():
                                return null
                                break;
                            case LiveFeedService.PATH_ROOT + PFOUR.id.toHexString():
                                return b4
                                break;
                        }
                        fail("Not sure how we got here")
                }
        ] as BroadcasterFactory
        listener.broadcasterFactory = factory
        listener.gameMasker = masker

        listener.gameChanged(game, PONE, initiatingServer)
        assert p2pub && p4pub
    }
}
