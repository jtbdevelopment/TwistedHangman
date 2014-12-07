package com.jtbdevelopment.TwistedHangman

import org.atmosphere.cpr.AtmosphereRequest
import org.atmosphere.cpr.AtmosphereServlet
import org.atmosphere.util.DefaultEndpointMapper
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.NetworkListener
import org.glassfish.grizzly.servlet.ServletRegistration
import org.glassfish.grizzly.servlet.WebappContext
import org.glassfish.grizzly.websockets.WebSocketAddOn
import org.glassfish.jersey.servlet.ServletContainer

/**
 * Date: 11/17/14
 * Time: 7:19 AM
 */
class GrizzlyServerBuilder {
    static HttpServer makeServer(final int port, final String springContext) {
        HttpServer server = HttpServer.createSimpleServer(".", port)

        // enable web socket support
        final WebSocketAddOn addon = new WebSocketAddOn();

        for (NetworkListener listener : server.getListeners()) {
            listener.registerAddOn(addon);
        }

        WebappContext context = new WebappContext("ctx", "/");

        //  TODO - hack - kept getting issue where websock pathinfo was null and unable to resolve it
        AtmosphereServlet atmosphereServlet = new AtmosphereServlet()
        atmosphereServlet.framework().endPointMapper(new DefaultEndpointMapper() {
            @Override
            String computePath(final AtmosphereRequest req) {
                return req.requestURI
            }
        })

        final ServletRegistration atmosphereServletRegistration = context.addServlet("AtmosphereServlet", atmosphereServlet);
        atmosphereServletRegistration.setInitParameter(
                "org.atmosphere.websocket.messageContentType",
                "application/json");
        atmosphereServletRegistration.setInitParameter("org.atmosphere.cpr.packages", "com.jtbdevelopment.TwistedHangman.rest.services")
        atmosphereServletRegistration.asyncSupported = true;
        atmosphereServletRegistration.addMapping("/livefeed/*");
        atmosphereServletRegistration.setLoadOnStartup(1);

        ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
        registration.addMapping("/api/*");
        registration.setInitParameter("jersey.config.server.provider.packages", "com.jtbdevelopment.TwistedHangman")
        registration.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.filter.LoggingFilter")
        registration.setInitParameter("jersey.config.server.tracing", "ALL")
        registration.setLoadOnStartup(2)

        context.addContextInitParameter("contextConfigLocation", "classpath:" + springContext);
        context.addListener("org.springframework.web.context.ContextLoaderListener");
        context.addListener("org.springframework.web.context.request.RequestContextListener");
        context.deploy(server);

        server.start()
        server;
    }

    static void main(final String[] args) throws Exception {

        makeServer(9998, "spring-context-rest.xml");
        Thread.sleep(Long.MAX_VALUE);
    }
}
