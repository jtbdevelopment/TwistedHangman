package com.jtbdevelopment.TwistedHangman.feed.websocket

import org.atmosphere.client.TrackMessageSizeInterceptor
import org.atmosphere.config.service.ManagedService
import org.atmosphere.config.service.Message
import org.atmosphere.config.service.Ready
import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor
import org.atmosphere.interceptor.SuspendTrackerInterceptor

import java.lang.reflect.Method

/**
 * Date: 12/22/14
 * Time: 7:41 PM
 */
class LiveFeedServiceTest extends GroovyTestCase {
    LiveFeedService liveFeedService = new LiveFeedService()

    void testOnConnect() {
        liveFeedService.id = "TESTID"
        String uuid = "UUID"
        AtmosphereResource resource = [
                uuid: {
                    return uuid
                }
        ] as AtmosphereResource
        WebSocketMessage message = liveFeedService.onReady(resource)
        assert message.messageType == WebSocketMessage.MessageType.Heartbeat
        assert message.game == null
        assert message.message == "connected to " + liveFeedService.id
    }

    void testOnMessageLoop() {
        WebSocketMessage message = new WebSocketMessage()
        assert message.is(liveFeedService.onMessage(message))
    }

    void testClassAnnotations() {
        ManagedService a = LiveFeedService.class.getAnnotation(ManagedService.class)
        assert a != null
        assert a.path() == "/livefeed/{id: [a-zA-Z][a-zA-Z_0-9]*}"
        assert a.interceptors() == [SpringSecuritySessionInterceptor.class,
                                    AtmosphereResourceLifecycleInterceptor.class,
                                    TrackMessageSizeInterceptor.class,
                                    SuspendTrackerInterceptor.class]
        assert a.atmosphereConfig() == ["supportSession=true"]
    }

    void testOnReadyAnnotations() {
        Method m = LiveFeedService.class.getMethod("onReady", [AtmosphereResource.class] as Class<?>[])
        assert m != null
        Ready r = m.getAnnotation(Ready.class)
        assert r != null
        assert r.encoders() == [HeartbeatJSON.class]
    }

    void testOnMessageAnnotations() {
        Method m = LiveFeedService.class.getMethod("onMessage", [WebSocketMessage.class] as Class<?>[])
        assert m != null
        Message message = m.getAnnotation(Message.class)
        assert message != null
        assert message.encoders() == [HeartbeatJSON.class]
        assert message.decoders() == [HeartbeatJSON.class]
    }
}
