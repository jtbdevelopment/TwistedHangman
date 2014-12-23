package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.publish.GameListener
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
class AtmosphereGameListener implements GameListener {
    @Autowired
    GameMasker gameMasker

    //  TODO - injectable in theory when 2.3 comes out, currently only a RC.  Replace getBroadcasterFactory then
    BroadcasterFactory broadcasterFactory

    @Override
    void gameChanged(final Game game, final Player initiatingPlayer) {
        game.players.findAll {
            Player p ->
                p != initiatingPlayer
        }.each {
            Player publish ->
                Broadcaster broadcaster = getBroadcasterFactory().lookup(LiveFeedService.PATH_ROOT + publish.id.toHexString())
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

    protected BroadcasterFactory getBroadcasterFactory() {
        if (!broadcasterFactory) {
            broadcasterFactory = BroadcasterFactory.default
        }
        broadcasterFactory
    }
}
