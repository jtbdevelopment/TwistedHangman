package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import com.jtbdevelopment.gamecore.exceptions.system.FailedToFindPlayersException
import org.bson.types.ObjectId

/**
 * Date: 11/10/14
 * Time: 6:56 PM
 */
class AbstractHandlerTest extends TwistedHangmanTestCase {
    private class TestHandler extends AbstractHandler {

    }
    TestHandler handler = new TestHandler()


    public void testLoadPlayer() {
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PTWO.id
                        return PTWO
                }
        ] as AbstractPlayerRepository<ObjectId>

        assert PTWO.is(handler.loadPlayer(PTWO.id))
    }


    public void testLoadPlayerFindsNull() {

        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PTWO.id
                        return null
                }
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.loadPlayer(PTWO.id)
        } catch (FailedToFindPlayersException e) {

        }
    }


}
