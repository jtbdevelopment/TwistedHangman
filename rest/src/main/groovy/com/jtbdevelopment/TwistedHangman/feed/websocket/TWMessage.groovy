package com.jtbdevelopment.TwistedHangman.feed.websocket

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
    Object message
}
