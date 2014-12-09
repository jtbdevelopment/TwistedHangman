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
class HeartbeatJSON implements Encoder<TWMessage, String>, Decoder<String, TWMessage> {
    public static final ObjectMapper mapper = new ObjectMapper()

    @Override
    String encode(final TWMessage input) {
        String string = mapper.writeValueAsString(input)
        return string
    }

    @Override
    TWMessage decode(final String s) {
        TWMessage message = mapper.readValue(s, TWMessage.class)
        return message
    }
}
