package com.jtbdevelopment.TwistedHangman

import groovy.transform.CompileStatic
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

        ServletHolder jerseyServlet = webAppContext.addServlet(ServletContainer.class, "/api/*")
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.jtbdevelopment.TwistedHangman");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.filter.LoggingFilter");
        jerseyServlet.setInitParameter("jersey.config.server.tracing", "ALL");

        webAppContext.addFilter(new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain")), "/*", EnumSet.allOf(DispatcherType.class));

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
