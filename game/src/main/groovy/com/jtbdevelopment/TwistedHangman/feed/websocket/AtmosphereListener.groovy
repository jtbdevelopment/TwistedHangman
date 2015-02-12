package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.publish.GameListener
import com.jtbdevelopment.TwistedHangman.publish.PlayerListener
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/8/14
 * Time: 7:56 PM
 */
@Component
@CompileStatic
class AtmosphereListener implements GameListener, PlayerListener {
    @Autowired
    GameMasker gameMasker

    @Autowired
    AbstractPlayerRepository playerRepository

    //  TODO - injectable in theory when 2.3 comes out, currently only a RC.  Replace getBroadcasterFactory then
    BroadcasterFactory broadcasterFactory

    @Override
    void gameChanged(final Game game, final Player initiatingPlayer) {
        game.players.findAll {
            Player p ->
                p != initiatingPlayer
        }.each {
            Player publish ->
                Broadcaster broadcaster = getBroadcasterFactory().lookup(LiveFeedService.PATH_ROOT + publish.idAsString)
                if (broadcaster != null) {
                    broadcaster.broadcast(
                            new WebSocketMessage(
                                    messageType: WebSocketMessage.MessageType.Game,
                                    game: gameMasker.maskGameForPlayer(game, publish)
                            )
                    )
                }
        }
    }

    @Override
    void playerChanged(final Player player) {
        Broadcaster broadcaster = getBroadcasterFactory().lookup(LiveFeedService.PATH_ROOT + player.idAsString)
        if (broadcaster != null) {
            broadcaster.broadcast(
                    new WebSocketMessage(
                            messageType: WebSocketMessage.MessageType.Player,
                            player: (MongoPlayer) player
                    )
            )
        }
    }

    @Override
    void allPlayersChanged() {
        getBroadcasterFactory().lookupAll().each {
            Broadcaster broadcaster ->
                Player p = (Player) playerRepository.findOne(broadcaster.ID.replace('/livefeed/', ''))
                if (p) {
                    broadcaster.broadcast(new WebSocketMessage(messageType: WebSocketMessage.MessageType.Player, player: p))
                }
        }
    }

    protected BroadcasterFactory getBroadcasterFactory() {
        if (!broadcasterFactory) {
            //  Explicitly not tested for now - see TODO above
            broadcasterFactory = BroadcasterFactory.default
        }
        broadcasterFactory
    }
}
