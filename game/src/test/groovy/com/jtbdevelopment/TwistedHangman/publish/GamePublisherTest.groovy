package com.jtbdevelopment.TwistedHangman.publish

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.gamecore.mongo.players.MongoPlayer

import java.util.concurrent.Callable
import java.util.concurrent.ThreadPoolExecutor

/**
 * Date: 12/8/14
 * Time: 7:11 PM
 */
class GamePublisherTest extends TwistedHangmanTestCase {
    GamePublisher publisher = new GamePublisher()


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

        Game g1 = makeSimpleGame("1")
        Game g2 = makeSimpleGame("2")
        MongoPlayer p1 = PONE
        MongoPlayer p2 = PTWO

        assert publisher.subscribers == null
        publisher.publish(g1, p2)
        publisher.publish(g2, p1)
        publisher.service.shutdown()

        //  Not crashing is success
    }

    //  TODO - this test has a timing issue and fails sometimes
    void testPublish() {
        publisher.threads = 1
        publisher.setUp()

        def games = []
        def players = []
        def gl1 = [
                gameChanged: {
                    Game g, MongoPlayer p ->
                        games.add(g)
                        players.add(p)
                },
        ] as GameListener
        def gl2 = [
                gameChanged: {
                    Game g, MongoPlayer p ->
                        games.add(g)
                        players.add(p)
                },
        ] as GameListener

        publisher.subscribers = [gl1, gl2]

        Game g1 = makeSimpleGame("1")
        Game g2 = makeSimpleGame("2")
        MongoPlayer p1 = PONE
        MongoPlayer p2 = PTWO

        publisher.publish(g1, p2)
        publisher.publish(g2, p1)
        publisher.service.shutdown()
        //  TODO - this test has a timing issue and fails sometimes
        assert games == [g1, g1, g2, g2]
        assert players == [p2, p2, p1, p1]
    }
}
