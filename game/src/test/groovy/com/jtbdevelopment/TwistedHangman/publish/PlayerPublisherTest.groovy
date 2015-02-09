package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.games.mongo.players.MongoPlayer

import java.util.concurrent.Callable
import java.util.concurrent.ThreadPoolExecutor

/**
 * Date: 2/6/15
 * Time: 7:02 PM
 */
class PlayerPublisherTest extends TwistedHangmanTestCase {
    PlayerPublisher publisher = new PlayerPublisher()

    void testCreatesExecutorService() {
        publisher.threads = 20
        publisher.setUp()
        def expectedResult = "RESULT"
        assert ((ThreadPoolExecutor) publisher.service).corePoolSize == 20
        assert publisher.service.submit(new Callable() {
            @Override
            Object call() throws Exception {
                Thread.sleep(1000)
                return expectedResult
            }
        }).get() == expectedResult
        publisher.service.shutdownNow()
    }

    void testPublishWithNoListeners() {
        publisher.threads = 1
        publisher.setUp()

        assert publisher.subscribers == null
        publisher.publish(PONE)
        publisher.publish(PTWO)
        publisher.service.shutdown()

        //  Not crashing is success
    }

    void testPublish() {
        publisher.threads = 1
        publisher.setUp()

        def players = []
        def gl1 = [
                playerChanged: {
                    MongoPlayer p ->
                        players.add(p)
                },
        ] as PlayerListener
        def gl2 = [
                playerChanged: {
                    MongoPlayer p ->
                        players.add(p)
                },
        ] as PlayerListener

        publisher.subscribers = [gl1, gl2]

        publisher.publish(PTWO)
        publisher.publish(PONE)
        Thread.sleep(1000);
        publisher.service.shutdown()
        assert players == [PTWO, PTWO, PONE, PONE]
    }
}
