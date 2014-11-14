package com.jtbdevelopment.TwistedHangman.rest.services

import org.glassfish.jersey.test.JerseyTest

/**
 * Date: 11/13/14
 * Time: 6:45 AM
 */
class GameServiceTest extends JerseyTest {
    GameService gameService = new GameService()

    void testPing() {
        assert GameService.PING_RESULT == gameService.ping()
    }

    void testCreateNewGame() {
        gameService.createNewGame()
    }

    void testCreateRematch() {

    }

    void testRejectGame() {

    }

    void testAcceptGame() {

    }

    void testSetPuzzle() {

    }

    void testStealLetter() {

    }

    void testGuessLetter() {

    }
}

