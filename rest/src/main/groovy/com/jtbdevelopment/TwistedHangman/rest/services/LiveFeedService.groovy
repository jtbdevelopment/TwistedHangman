package com.jtbdevelopment.TwistedHangman.rest.services

import groovy.transform.CompileStatic
import org.atmosphere.config.service.Disconnect
import org.atmosphere.config.service.ManagedService
import org.atmosphere.config.service.PathParam
import org.atmosphere.config.service.Ready
import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.cpr.AtmosphereResourceEvent
import org.atmosphere.cpr.AtmosphereResourceFactory
import org.atmosphere.cpr.BroadcasterFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Extremely simple chat application supporting WebSocket, Server Side-Events, Long-Polling and Streaming.
 *
 * @author Jeanfrancois Arcand
 */
@ManagedService(path = "/livefeed/{id: [a-zA-Z][a-zA-Z_0-9]*}")
@CompileStatic
public class LiveFeedService {

    static final Logger logger = LoggerFactory.getLogger(LiveFeedService.class)

    @PathParam("id")
    String id

    private BroadcasterFactory broadcasterFactory
    private AtmosphereResourceFactory resourceFactory;

    public LiveFeedService() {
        logger.info("LiveFeedService instantiated")
    }

    @Ready()
    @SuppressWarnings("unused")
    public String onReady(final AtmosphereResource r) {
        logger.info("Browser {} connected.", r.uuid());

        broadcasterFactory = r.getAtmosphereConfig().getBroadcasterFactory();
        resourceFactory = r.getAtmosphereConfig().resourcesFactory();

        return '{"msg": "connected to ' + id + '"}'
    }

    @Disconnect
    @SuppressWarnings("unused")
    public void onDisconnect(final AtmosphereResourceEvent event) {
        if (event.isCancelled()) {
            // We didn't get notified, so we remove the user.
            logger.info("Browser {} unexpectedly disconnected", event.getResource().uuid());
        } else if (event.isClosedByClient()) {
            logger.info("Browser {} closed the connection", event.getResource().uuid());
        }
    }
}