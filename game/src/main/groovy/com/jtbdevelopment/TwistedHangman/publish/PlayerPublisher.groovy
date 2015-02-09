package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Date: 12/8/14
 * Time: 6:40 PM
 */
@Component
@CompileStatic
class PlayerPublisher {
    @Autowired(required = false)
    List<PlayerListener> subscribers

    @Value('${publishing.threads:10}')
    int threads

    ExecutorService service;

    //  Returns game primarily to allow easy chaining
    void publish(final Player player) {
        service.submit(new Runnable() {
            @Override
            void run() {
                if (subscribers != null) {
                    subscribers.each {
                        PlayerListener listener ->
                            listener.playerChanged(player)
                    }
                }
            }
        })
    }


    @PostConstruct
    public void setUp() {
        service = Executors.newFixedThreadPool(threads)
    }
}