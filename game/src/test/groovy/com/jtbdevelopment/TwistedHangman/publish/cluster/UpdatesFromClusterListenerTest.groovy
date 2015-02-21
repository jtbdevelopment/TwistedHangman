package com.jtbdevelopment.TwistedHangman.publish.cluster

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.TwistedHangman.publish.PlayerPublisher
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.games.Game
import com.jtbdevelopment.games.players.Player

/**
 * Date: 2/21/15
 * Time: 6:24 PM
 */
class UpdatesFromClusterListenerTest extends TwistedHangmanTestCase {
    private UpdatesFromClusterListener listener = new UpdatesFromClusterListener()

    void testReceivePublishAllPlayers() {
        boolean published = false
        listener.playerPublisher = [
                publishAll: {
                    boolean fromServer ->
                        assertFalse fromServer
                        published = true
                }
        ] as PlayerPublisher
        listener.receivePublishAllPlayers()
        assert published
    }

    void testReceivePublishPlayer() {
        boolean published = false
        listener.playerRepository = [
                findOne: {
                    String id ->
                        assert id == PTWO.idAsString
                        return PTWO
                }
        ] as AbstractPlayerRepository
        listener.playerPublisher = [
                publish: {
                    Player p, boolean fromServer ->
                        assert p.is(PTWO)
                        assertFalse fromServer
                        published = true
                }
        ] as PlayerPublisher
        listener.receivePublishPlayer(PTWO.idAsString)
        assert published
    }

    void testReceivePublishGame() {
        String gameId = 'GID'
        boolean published = false
        Game game = [] as Game
        listener.playerRepository = [
                findOne: {
                    String id ->
                        assert id == PTHREE.idAsString
                        return PTHREE
                }
        ] as AbstractPlayerRepository
        listener.gameRepository = [
                findOne: {
                    String id ->
                        assert id == gameId
                        return game
                }
        ] as GameRepository
        listener.gamePublisher = [
                publish: {
                    Game g, Player p, boolean fromServer ->
                        assert p.is(PTHREE)
                        assert g.is(game)
                        assertFalse fromServer
                        published = true
                        return g
                }
        ] as GamePublisher
        listener.receivePublishGame(gameId, PTHREE.idAsString)
        assert published
    }
}
