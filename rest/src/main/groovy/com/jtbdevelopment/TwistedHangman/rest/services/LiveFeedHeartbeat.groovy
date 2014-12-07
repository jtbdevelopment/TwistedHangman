package com.jtbdevelopment.TwistedHangman.rest.services

import groovy.transform.CompileStatic
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.concurrent.Future

/**
 * Date: 12/6/14
 * Time: 4:48 PM
 */
@Component
@Lazy(false)
@CompileStatic
class LiveFeedHeartbeat {
    volatile boolean publish = true

    @PostConstruct
    public void setUp() {
        Thread t = new Thread(new Runnable() {
            @Override
            void run() {
                while (true) {
                    Thread.sleep(10000)
                    try {
                        if (publish) {
                            BroadcasterFactory factory = BroadcasterFactory.default;
                            Broadcaster broadcaster = factory.lookup("/livefeed")

                            Future<Object> f = broadcaster.broadcast('{"msg": "ping"}');
                            Object o = f.get();
                            println o
                        }
                    } catch (Throwable e) {
                        print e;
                    }
                }
            }
        })
        t.start()
    }

}