package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.jtbdevelopment.spring.jackson.ObjectMapperFactory
import groovy.transform.CompileStatic
import org.atmosphere.config.managed.Decoder
import org.atmosphere.config.managed.Encoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/8/14
 * Time: 6:52 AM
 */
@CompileStatic
@Component
class WebSocketJSONConverter implements Encoder<WebSocketMessage, String>, Decoder<String, WebSocketMessage> {
    protected static ObjectMapper mapper

    @SuppressWarnings("GrMethodMayBeStatic")
    @Autowired
    void setObjectMapperFactory(final ObjectMapperFactory objectMapperFactory) {
        mapper = objectMapperFactory.objectMapper
    }

    @Override
    String encode(final WebSocketMessage input) {
        return mapper?.writeValueAsString(input)
    }

    @Override
    WebSocketMessage decode(final String s) {
        return mapper?.readValue(s, WebSocketMessage.class)
    }
}