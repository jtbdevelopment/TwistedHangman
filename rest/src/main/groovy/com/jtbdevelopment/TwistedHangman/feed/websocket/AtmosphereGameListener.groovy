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
//  TODO - test and remove noop listener
class AtmosphereGameListener implements GameListener {
    @Autowired
    GameMasker gameMasker

    //  TODO - springify Atmosphere?  There are extensions
    BroadcasterFactory broadcasterFactory

    @Override
    void gameChanged(final Game game, final Player initiatingPlayer) {
        game.players.findAll {
            Player p ->
                p != initiatingPlayer
        }.each {
            Player p ->
                //  TODO - this will need to be a lookup when we encode it
                Broadcaster broadcaster = getBroadcasterFactory().lookup("/livefeed/" + p.id)
                if (broadcaster != null) {
                    broadcaster.broadcast(
                            new TWMessage(
                                    messageType: TWMessage.MessageType.Game,
                                    message: gameMasker.maskGameForPlayer(game, p)
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
