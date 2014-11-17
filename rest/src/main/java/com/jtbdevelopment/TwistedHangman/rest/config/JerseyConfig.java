package com.jtbdevelopment.TwistedHangman.rest.config;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Date: 11/16/2014
 * Time: 3:50 PM
 */
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(final String configFile) {
        register(RequestContextFilter.class);
        packages("com.jtbdevelopment");
        property("contextConfigLocation", "classpath:" + configFile);
        register(JacksonFeature.class);
        register(LoggingFilter.class);
    }
}
