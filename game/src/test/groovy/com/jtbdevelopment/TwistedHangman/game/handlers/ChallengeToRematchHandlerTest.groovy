package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotAvailableToRematchException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/11/14
 * Time: 6:54 PM
 */
class ChallengeToRematchHandlerTest extends TwistedHangmanTestCase {
    ChallengeToRematchHandler handler = new ChallengeToRematchHandler()


    public void testSetsUpRematch() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"))
        Thread.sleep(100);
        Game previous = new Game(gamePhase: GamePhase.RoundOver, id: "X")
        Game previousT = previous.clone()
        Game previousS = previous.clone()
        Game previousP = previous.clone()
        Game newGame = new Game(previousId: previous.id)
        Game puzzled = newGame.clone()
        handler.gameFactory = [
                createGame: {
                    Game g, Player p ->
                        assert g.is(previousP)
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
                        return previousS
                }
        ] as GameRepository
        handler.gamePublisher = [
                publish: {
                    Game g, Player p ->
                        assert g.is(previousS)
                        assert p.is(Player.SYSTEM_PLAYER)
                        previousP
                }
        ] as GamePublisher
        handler.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    Game g ->
                        assert g.is(newGame)
                        return puzzled
                }
        ] as SystemPuzzlerSetter

        puzzled.is(handler.handleActionInternal(PONE, previous, null))
    }


    public void testNotInRematchPhase() {
        GamePhase.values().find { it != GamePhase.RoundOver }.each {
            Game previous = new Game(gamePhase: it, id: "X")
            try {
                handler.handleActionInternal(PONE, previous, null)
                fail("Should have exceptioned in phase " + it)
            } catch (GameIsNotAvailableToRematchException e) {
                //
            }
        }
    }
}
