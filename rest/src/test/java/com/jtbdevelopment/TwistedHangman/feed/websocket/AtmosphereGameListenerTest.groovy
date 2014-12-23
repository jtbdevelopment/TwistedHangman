package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory

/**
 * Date: 12/22/14
 * Time: 7:18 PM
 */
class AtmosphereGameListenerTest extends TwistedHangmanTestCase {

    AtmosphereGameListener listener = new AtmosphereGameListener()

    void testPublishToConnectedNonInitiatingPlayers() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        boolean p2pub = false;
        boolean p4pub = false;
        MaskedGame mg2 = new MaskedGame()
        MaskedGame mg4 = new MaskedGame()
        Broadcaster b2 = [
                broadcast: {
                    Object o ->
                        assert o in TWMessage
                        assert o.messageType == TWMessage.MessageType.Game
                        assert o.game.is(mg2)
                        assert o.message == null
                        p2pub = true
                        null
                }
        ] as Broadcaster
        Broadcaster b4 = [
                broadcast: {
                    Object o ->
                        assert o in TWMessage
                        assert o.messageType == TWMessage.MessageType.Game
                        assert o.game.is(mg4)
                        assert o.message == null
                        p4pub = true
                        null
                }
        ] as Broadcaster
        GameMasker masker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert game.is(g)
                        if (p == PTWO) return mg2
                        if (p == PFOUR) return mg4
                        fail("Masking for unexpected player")
                }
        ] as GameMasker
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

        listener.gameChanged(game, PONE)
        assert p2pub && p4pub
    }
}
