package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotAvailableToRematchException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import org.bson.types.ObjectId

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/11/14
 * Time: 6:54 PM
 */
class ChallengeToRematchHandlerTest extends TwistedHangmanTestCase {
    ChallengeToRematchHandler handler = new ChallengeToRematchHandler()

    void testEligibilityCheck() {
        assert handler.requiresEligibilityCheck(null)
        assert handler.requiresEligibilityCheck('')
        assert handler.requiresEligibilityCheck(1L)
    }

    public void testSetsUpRematch() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"))
        Thread.sleep(100);
        Game previous = new Game(gamePhase: GamePhase.RoundOver, id: new ObjectId())
        Game previousT = previous.clone()
        Game previousS = previous.clone()
        Game previousP = previous.clone()
        Game newGame = new Game(previousId: previous.id)
        Game puzzled = newGame.clone()
        handler.gameFactory = [
                createGame: {
                    Game g, MongoPlayer p ->
                        assert g.is(previousP)
                        assert p.is(PONE)
                        newGame
                }
        ] as GameFactory
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game g ->
                        assert g.is(previous)
                        assert g.rematchTimestamp != null
                        assert now < g.rematchTimestamp
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
                    Game g, MongoPlayer p ->
                        assert g.is(previousS)
                        assert p.is(TwistedHangmanSystemPlayerCreator.TH_PLAYER)
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

    public void testNoRotation() {
        GamePhase.values().each {
            GamePhase it ->
                def ps = [PONE, PTWO, PFOUR]
                Game game = new Game(
                        gamePhase: it,
                        players: ps,
                        features: [GameFeature.TurnBased],
                        featureData: [(GameFeature.TurnBased): PONE.id])
                assert game.is(handler.rotateTurnBasedGame(game));
                assert game.players[0] == PONE
                assert game.players[1] == PTWO
                assert game.players[2] == PFOUR
                assert game.featureData[GameFeature.TurnBased] == PONE.id
        }
    }

    public void testNotInRematchPhase() {
        GamePhase.values().find { it != GamePhase.RoundOver }.each {
            Game previous = new Game(gamePhase: it, id: new ObjectId())
            try {
                handler.handleActionInternal(PONE, previous, null)
                fail("Should have exceptioned in phase " + it)
            } catch (GameIsNotAvailableToRematchException e) {
                //
            }
        }
    }
}
