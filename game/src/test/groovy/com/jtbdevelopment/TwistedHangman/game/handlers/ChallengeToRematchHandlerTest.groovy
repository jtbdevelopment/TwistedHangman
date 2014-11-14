package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotAvailableToRematchException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/11/14
 * Time: 6:54 PM
 */
class ChallengeToRematchHandlerTest extends TwistedHangmanTestCase {
    ChallengeToRematchHandler handler = new ChallengeToRematchHandler()

    @Test
    public void testSetsUpRematch() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"))
        Thread.sleep(100);
        Game previous = new Game(gamePhase: Game.GamePhase.Rematch, id: "X")
        Game previousT = previous.clone()
        Game newGame = new Game(previousId: previous.id)
        Game newSaved = newGame.clone()
        Game puzzled = newSaved.clone()
        handler.gameFactory = [
                createGame: {
                    Game g, Player p ->
                        assert g.is(newSaved)
                        assert p.is(PONE)
                        newGame
                }
        ] as GameFactory
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game g ->
                        assert g.is(previous)
                        assert g.rematched != null
                        assert now < g.rematched
                        return previousT
                }
        ] as GamePhaseTransitionEngine
        handler.gameRepository = [
                save: {
                    Game g ->
                        assert g.is(previousT)
                        return newSaved
                }
        ] as GameRepository
        handler.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    Game g ->
                        assert g.is(newGame)
                        return puzzled
                }
        ] as SystemPuzzlerSetter

        puzzled.is(handler.handleActionInternal(PONE, previous))
    }

    @Test
    public void testNotInRematchPhase() {
        Game.GamePhase.values().find { it != Game.GamePhase.Rematch }.each {
            Game previous = new Game(gamePhase: it, id: "X")
            try {
                handler.handleActionInternal(PONE, previous)
                fail("Should have exceptioned in phase " + it)
            } catch (GameIsNotAvailableToRematchException e) {
                //
            }
        }
    }
}
