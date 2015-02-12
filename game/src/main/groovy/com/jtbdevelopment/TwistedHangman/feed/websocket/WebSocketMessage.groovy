package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic

/**
 * Date: 12/8/14
 * Time: 6:59 AM
 */
@CompileStatic
class WebSocketMessage {
    public enum MessageType {
        Heartbeat,      //  Check message
        Game,           //  Check game
        Player,         //  Check player
        Alert,          //  Check message
    }

    MessageType messageType
    MaskedGame game
    Player player
    String message
}
