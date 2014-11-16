package com.jtbdevelopment.TwistedHangman.rest;


import com.jtbdevelopment.TwistedHangman.rest.config.JerseyConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Date: 11/11/14
 * Time: 10:12 PM
 */
public class ExampleServer {
    public static void main(final String[] args) throws Exception {

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        ResourceConfig config = new JerseyConfig("spring-context-rest.xml");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        Thread.sleep(Long.MAX_VALUE);
    }

}