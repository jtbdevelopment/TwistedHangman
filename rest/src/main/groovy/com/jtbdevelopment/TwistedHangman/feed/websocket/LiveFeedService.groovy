package com.jtbdevelopment.TwistedHangman.feed.websocket

import groovy.transform.CompileStatic
import org.atmosphere.config.service.*
import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.cpr.AtmosphereResourceEvent
import org.atmosphere.cpr.AtmosphereResourceFactory
import org.atmosphere.cpr.BroadcasterFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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

    @Ready(encoders = [HeartbeatJSON.class])
    @SuppressWarnings("unused")
    public TWMessage onReady(final AtmosphereResource r) {
        logger.info("Browser {} connected.", r.uuid());

        broadcasterFactory = r.getAtmosphereConfig().getBroadcasterFactory();
        resourceFactory = r.getAtmosphereConfig().resourcesFactory();

        return new TWMessage(messageType: TWMessage.MessageType.Heartbeat, message: "connected to" + id)
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

    @Message(decoders = [HeartbeatJSON.class], encoders = [HeartbeatJSON.class])
    public TWMessage onMessage(TWMessage message) {
        return message
    }
}