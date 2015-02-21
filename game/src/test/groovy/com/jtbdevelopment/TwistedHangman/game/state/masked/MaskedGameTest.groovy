package com.jtbdevelopment.TwistedHangman.game.state.masked

/**
 * Date: 2/19/15
 * Time: 6:56 PM
 */
class MaskedGameTest extends GroovyTestCase {
    void testInit() {
        MaskedGame maskedGame = new MaskedGame()

        assertNull maskedGame.rematchTimestamp
        assertNull maskedGame.gamePhase
        assertNull maskedGame.round
        assertNull maskedGame.wordPhraseSetter
        assert maskedGame.playerRoundScores.isEmpty()
        assert maskedGame.playerRunningScores.isEmpty()
        assert maskedGame.solverStates.isEmpty()
    }
}
