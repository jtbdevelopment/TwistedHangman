package com.jtbdevelopment.TwistedHangman

import groovy.transform.CompileStatic
import org.atmosphere.cpr.AtmosphereServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.webapp.WebAppContext
import org.glassfish.jersey.servlet.ServletContainer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.filter.DelegatingFilterProxy

import javax.servlet.DispatcherType

/**
 * Date: 12/20/2014
 * Time: 3:24 PM
 */
@CompileStatic
class JettyServer {
    static Server makeServer(int port, final String springContext) {
        Server server = new Server(port)

        WebAppContext webAppContext = new WebAppContext()
        webAppContext.setInitParameter("contextConfigLocation", "classpath:" + springContext);
        webAppContext.setResourceBase(".")
        webAppContext.setContextPath("/")
        webAppContext.setParentLoaderPriority(false)
        webAppContext.addEventListener(new ContextLoaderListener())
        webAppContext.addEventListener(new RequestContextListener())
/*
        <servlet>
        <description>AtmosphereServlet</description>
        <servlet-name>AtmosphereServlet</servlet-name>
        <servlet-class>org.atmosphere.cpr.AtmosphereServlet</servlet-class>
        <init-param>
            <param-name>org.atmosphere.cpr.packages</param-name>
        <param-value>com.jtbdevelopment.TwistedHangman</param-value>
        </init-param>
        <init-param>
        <param-name>org.atmosphere.websocket.messageContentType</param-name>
            <param-value>application/json</param-value>
        </init-param>
        <init-param>
        <param-name>org.atmosphere.cpr.broadcasterLifeCyclePolicy</param-name>
            <param-value>EMPTY_DESTROY</param-value>
        </init-param>
        <init-param>
            <param-name>org.atmosphere.cpr.sessionSupport</param-name>
        <param-value>true</param-value>
        </init-param>
        <async-supported>true</async-supported>
        <load-on-startup>0</load-on-startup>
        </servlet>
*/
        ServletHolder atmosphereServletHolder = webAppContext.addServlet(AtmosphereServlet.class, "/livefeed/*")
        atmosphereServletHolder.setInitOrder(1)
        atmosphereServletHolder.setInitParameter("org.atmosphere.cpr.packages", "com.jtbdevelopment")
        atmosphereServletHolder.setInitParameter("org.atmosphere.websocket.messageContentType", "application/json")
        atmosphereServletHolder.setInitParameter("org.atmosphere.cpr.broadcasterLifeCyclePolicy", "EMPTY_DESTROY")
        atmosphereServletHolder.setInitParameter("org.atmosphere.cpr.sessionSupport", "true")
        atmosphereServletHolder.asyncSupported = true

        ServletHolder jerseyServlet = webAppContext.addServlet(ServletContainer.class, "/api/*")
        jerseyServlet.setInitOrder(2);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.jtbdevelopment");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.filter.LoggingFilter");
        jerseyServlet.setInitParameter("jersey.config.server.tracing", "ALL");

        webAppContext.addFilter(new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain")), "/*", EnumSet.allOf(DispatcherType.class))

        server.setHandler(webAppContext)

        // Add Spring Security Filter by the name
        server
    }

    static void main(final String[] args) throws Exception {
        Server server = makeServer(9998, "spring-context-rest.xml");
        server.start()
        Thread.sleep(Long.MAX_VALUE);
        server.stop()
    }
}
