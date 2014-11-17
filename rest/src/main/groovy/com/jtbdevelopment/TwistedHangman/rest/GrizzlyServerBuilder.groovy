package com.jtbdevelopment.TwistedHangman.rest

import com.jtbdevelopment.TwistedHangman.rest.config.JerseyConfig
import org.glassfish.grizzly.http.server.CLStaticHttpHandler
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig

import javax.ws.rs.core.UriBuilder

/**
 * Date: 11/17/14
 * Time: 7:19 AM
 */
class GrizzlyServerBuilder {
    static HttpServer makeServer(final URI baseUri, final String context) {
        ResourceConfig config = new JerseyConfig(context)
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config)
        server.getServerConfiguration()
                .addHttpHandler(new CLStaticHttpHandler(GrizzlyServerBuilder.class.getClassLoader(), "/static/"))
        server;
    }

    static void main(final String[] args) throws Exception {

        URI baseUri = UriBuilder.fromUri("http://localhost/api").port(9998).build();
        GrizzlyServerBuilder.makeServer(baseUri, "spring-context-rest.xml");
        Thread.sleep(Long.MAX_VALUE);
    }
}
