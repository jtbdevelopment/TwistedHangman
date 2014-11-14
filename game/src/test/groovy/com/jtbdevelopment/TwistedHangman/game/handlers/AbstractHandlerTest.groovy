package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException

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
                    String it ->
                        assert it == PTWO.id
                        return PTWO
                }
        ] as PlayerRepository

        assert PTWO.is(handler.loadPlayer(PTWO.id))
    }


    public void testLoadPlayerFindsNull() {

        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PTWO.id
                        return null
                }
        ] as PlayerRepository

        try {
            handler.loadPlayer(PTWO.id)
        } catch (FailedToFindPlayersException e) {

        }
    }


    public void testLoadPlayers() {
        handler.playerRepository = [
                findAll: {
                    Iterable<String> it ->
                        assert [PONE.id, PTWO.id, PTHREE.id] as Set == it.collect { it } as Set
                        return [PONE, PTWO, PTHREE]
                }
        ] as PlayerRepository

        assert [PONE, PTWO, PTHREE] as Set == handler.loadPlayers([PONE.id, PTWO.id, PTHREE.id] as Set)
    }


    public void testLoadPlayersFindsNull() {
        handler.playerRepository = [
                findAll: {
                    Iterable<String> it ->
                        assert [PONE.id, PTWO.id, PTHREE.id] as Set == it.collect { it } as Set
                        return [PONE, PTHREE]
                }
        ] as PlayerRepository

        try {
            handler.loadPlayers([PONE.id, PTWO.id, PTHREE.id] as Set)
        } catch (FailedToFindPlayersException e) {

        }
    }
}
