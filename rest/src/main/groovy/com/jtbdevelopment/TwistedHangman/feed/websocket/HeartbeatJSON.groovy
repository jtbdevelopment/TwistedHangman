package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.atmosphere.config.managed.Decoder
import org.atmosphere.config.managed.Encoder

/**
 * Date: 12/8/14
 * Time: 6:52 AM
 */
@CompileStatic
class HeartbeatJSON implements Encoder<WebSocketMessage, String>, Decoder<String, WebSocketMessage> {
    private static final ObjectMapper mapper = new ObjectMapper()

    @Override
    String encode(final WebSocketMessage input) {
        String string = mapper.writeValueAsString(input)
        return string
    }

    @Override
    WebSocketMessage decode(final String s) {
        WebSocketMessage message = mapper.readValue(s, WebSocketMessage.class)
        return message
    }
}
