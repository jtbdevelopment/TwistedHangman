package com.jtbdevelopment.TwistedHangman.rest.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import groovy.transform.CompileStatic

import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

@Provider
@CompileStatic
class JacksonConfig implements ContextResolver<ObjectMapper> {
    private final ObjectMapper defaultObjectMapper

    public JacksonConfig() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule simpleModule = new SimpleModule("TwistedHangmanModule")
        result.registerModule(simpleModule)
        return result;
    }
}
