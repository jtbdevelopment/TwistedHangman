package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.exceptions.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/10/14
 * Time: 7:06 PM
 */
class AbstractGameActionHandlerTest extends THGroovyTestCase {
    private static final String testParam = "TESTPARAM"
    private static final Game handledGame = new Game();
    private final Game gameParam = new Game()
    private final String gameId = "GAMEID"

    private class TestHandler extends AbstractGameActionHandler<String> {
        @Override
        protected Game handleActionInternal(final Player player, final Game game, final String param) {
            assert param == testParam
            assert gameParam.is(game)
            return handledGame
        }
    }
    private TestHandler handler = new TestHandler()

    @Test
    public void testAbstractHandlerBase() {
        Game saved = new Game();
        Game transitioned = new Game();
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(handledGame)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(saved)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine

        assert transitioned.is(handler.handleAction(PONE.id, gameId, testParam))
    }

    @Test
    public void testAbstractHandlerCantLoadGame() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
                        assert it == gameId
                        return null
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail("should have failed")
        } catch (FailedToFindGameException e) {

        }
    }

    @Test
    public void testAbstractHandlerInvalidPlayer() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PTHREE.id
                        return PTHREE
                }
        ] as PlayerRepository

        try {
            handler.handleAction(PTHREE.id, gameId, testParam)
            fail("should have failed")
        } catch (PlayerNotPartOfGameException e) {

        }
    }
}
