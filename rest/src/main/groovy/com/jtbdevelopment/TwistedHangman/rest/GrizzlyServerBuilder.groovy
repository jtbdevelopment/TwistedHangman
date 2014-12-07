package com.jtbdevelopment.TwistedHangman.rest

import org.atmosphere.cpr.AtmosphereServlet
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.NetworkListener
import org.glassfish.grizzly.servlet.ServletRegistration
import org.glassfish.grizzly.servlet.WebappContext
import org.glassfish.grizzly.websockets.WebSocketAddOn
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.servlet.ServletContainer

import javax.ws.rs.core.UriBuilder

/**
 * Date: 11/17/14
 * Time: 7:19 AM
 */
class GrizzlyServerBuilder {
    static HttpServer makeServer(final URI baseUri, final String springContext) {
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri)

        // enable web socket support
        final WebSocketAddOn addon = new WebSocketAddOn();

        for (NetworkListener listener : server.getListeners()) {
            listener.registerAddOn(addon);
        }
        WebappContext context = new WebappContext("ctx", "");

        final AtmosphereServlet atmosphereServlet = new AtmosphereServlet();
        final ServletRegistration atmosphereServletRegistration = context.addServlet("AtmosphereServlet", atmosphereServlet);
        atmosphereServletRegistration.setInitParameter(
                "org.atmosphere.websocket.messageContentType",
                "application/json");
        atmosphereServletRegistration.addMapping("/livefeed/*");
        atmosphereServletRegistration.setLoadOnStartup(0);

        ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
        registration.addMapping("/api/*");
        registration.setInitParameter("jersey.config.server.provider.packages", "com.jtbdevelopment.TwistedHangman")
        registration.setLoadOnStartup(1)

        context.addContextInitParameter("contextConfigLocation", "classpath:" + springContext);
        context.addListener("org.springframework.web.context.ContextLoaderListener");
        context.addListener("org.springframework.web.context.request.RequestContextListener");
        context.deploy(server);

        server;
    }

    static void main(final String[] args) throws Exception {

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        GrizzlyServerBuilder.makeServer(baseUri, "spring-context-rest.xml");
        Thread.sleep(Long.MAX_VALUE);
    }
}
