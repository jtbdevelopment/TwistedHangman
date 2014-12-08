package com.jtbdevelopment.TwistedHangman.feed.websocket

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.atmosphere.config.managed.Decoder
import org.atmosphere.config.managed.Encoder

/**
 * Date: 12/8/14
 * Time: 6:52 AM
 */
@CompileStatic
class HeartbeatJSON implements Encoder<TWMessage, String>, Decoder<String, TWMessage> {


    @Override
    String encode(final TWMessage input) {
        String string = new JsonBuilder(input).toPrettyString()
        println string
        return string
    }

    @Override
    TWMessage decode(final String s) {
        println s

        TWMessage message = (TWMessage) new JsonSlurper().parseText(s)

        println message
        return message
    }
}
