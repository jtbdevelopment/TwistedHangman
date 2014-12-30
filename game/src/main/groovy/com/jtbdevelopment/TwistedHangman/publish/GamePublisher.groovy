package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
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
class GamePublisher {
    @Autowired(required = false)
    List<GameListener> subscribers

    @Value('${publishing.threads:10}')
    int threads

    ExecutorService service;

    //  Returns game primarily to allow easy chaining
    Game publish(final Game game, final Player initiatingPlayer) {
        service.submit(new Runnable() {
            @Override
            void run() {
                if (subscribers != null) {
                    subscribers.each {
                        GameListener listener ->
                            listener.gameChanged(game, initiatingPlayer)
                    }
                }
            }
        })
        game
    }


    @PostConstruct
    public void setUp() {
        service = Executors.newFixedThreadPool(threads)
    }
}
