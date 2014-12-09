package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame

/**
 * Date: 12/8/14
 * Time: 6:59 AM
 */
class TWMessage {
    public enum MessageType {
        Heartbeat,
        Game,
    }

    MessageType messageType
    MaskedGame game
    String message
}
