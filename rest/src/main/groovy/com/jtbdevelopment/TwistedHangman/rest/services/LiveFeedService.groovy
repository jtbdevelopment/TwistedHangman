package com.jtbdevelopment.TwistedHangman.rest.services

import org.atmosphere.client.TrackMessageSizeInterceptor
import org.atmosphere.config.service.AtmosphereService
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

/**
 * Extremely simple chat application supporting WebSocket, Server Side-Events, Long-Polling and Streaming.
 *
 * @author Jeanfrancois Arcand
 */
@Path("/")
@AtmosphereService(
        dispatch = false,
        interceptors = [AtmosphereResourceLifecycleInterceptor.class, TrackMessageSizeInterceptor.class],
        path = "/livefeed",
        servlet = "org.glassfish.jersey.servlet.ServletContainer")
public class LiveFeedService {

    //  TODO - sessions should get unique ids to subscribe to
    @Context
    private HttpServletRequest request;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String liveFeedPing() {
        return "liveFeed is Alive."
    }
}