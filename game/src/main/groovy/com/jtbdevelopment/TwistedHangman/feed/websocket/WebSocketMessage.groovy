package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import groovy.transform.CompileStatic

/**
 * Date: 12/8/14
 * Time: 6:59 AM
 */
@CompileStatic
class WebSocketMessage {
    public enum MessageType {
        Heartbeat,
        Game,
        Player,
    }

    MessageType messageType
    MaskedGame game
    MongoPlayer player
    String message
}
