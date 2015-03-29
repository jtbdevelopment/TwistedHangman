package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.PlayerGameTracker
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.exceptions.input.OutOfGamesForTodayException
import com.jtbdevelopment.games.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.games.exceptions.system.FailedToFindGameException
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.masking.MultiPlayerGameMasker
import com.jtbdevelopment.games.tracking.PlayerGameEligibility
import com.jtbdevelopment.games.tracking.PlayerGameEligibilityResult
import org.bson.types.ObjectId

/**
 * Date: 11/10/14
 * Time: 7:06 PM
 */
class AbstractPlayerRotatingGameActionHandlerTest extends TwistedHangmanTestCase {
    private static final String testParam = "TESTPARAM"
    private Game handledGame = new Game();
    private final Game gameParam = new Game()
    private final ObjectId gameId = new ObjectId();

    private class TestHandler extends AbstractPlayerRotatingGameActionHandler<String> {
        boolean checkEligibility = false
        boolean internalException = false

        @Override
        protected boolean requiresEligibilityCheck(final String param) {
            return checkEligibility
        }

        @Override
        protected Game handleActionInternal(
                final Player player, final Game game, final String param) {
            assert param == testParam
            assert gameParam.is(game)
            if (internalException) {
                throw new IllegalStateException()
            }
            return handledGame
        }
    }
    private TestHandler handler = new TestHandler()


    void testDefaultRequiresEligibility() {
        assertFalse new AbstractPlayerRotatingGameActionHandler<String>() {
            protected Game handleActionInternal(final Player player, final Game game, final String param) {
            }
        }.requiresEligibilityCheck(null)
    }

    public void testAbstractHandlerBasic() {
        Game saved = new Game();
        Game transitioned = new Game();
        Game published = new Game();
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }

    public void testAbstractHandlerWithEligibilityCheckAndEligible() {
        handler.checkEligibility = true
        Game saved = new Game();
        Game transitioned = new Game();
        Game published = new Game();
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.gameTracker = [
                getGameEligibility: {
                    Player p ->
                        assert p.is(PONE)
                        return new PlayerGameEligibilityResult(eligibility: PlayerGameEligibility.FreeGameUsed)
                }
        ] as PlayerGameTracker
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }

    public void testAbstractHandlerWithEligibilityCheckAndNotEligible() {
        handler.checkEligibility = true
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.gameTracker = [
                getGameEligibility: {
                    Player p ->
                        assert p.is(PONE)
                        return new PlayerGameEligibilityResult(eligibility: PlayerGameEligibility.NoGamesAvailable)
                }
        ] as PlayerGameTracker
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail('should have exceptioned')
        } catch (OutOfGamesForTodayException e) {
            //
        }
    }

    public void testAbstractHandlerWithEligibilityCheckAndHandleInternalExceptions() {
        handler.checkEligibility = true
        handler.internalException = true
        boolean revertCalled = false
        def eligibilityResult = new PlayerGameEligibilityResult(eligibility: PlayerGameEligibility.FreeGameUsed)
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.gameTracker = [
                getGameEligibility   : {
                    Player p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                        return
                }
        ] as PlayerGameTracker
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail('should have exception')
        } catch (IllegalStateException e) {
            assert revertCalled
        }
    }

    public void testAbstractHandlerWithEligibilityCheckAndTransitionExceptions() {
        handler.checkEligibility = true
        gameParam.players = [PONE, PTWO]
        boolean revertCalled = false
        def eligibilityResult = new PlayerGameEligibilityResult(eligibility: PlayerGameEligibility.FreeGameUsed)
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.gameTracker = [
                getGameEligibility   : {
                    Player p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                        return
                }
        ] as PlayerGameTracker
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        throw new IllegalArgumentException()
                }
        ] as GamePhaseTransitionEngine

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail('should have exceptioned')
        } catch (IllegalArgumentException e) {
            assert revertCalled
        }
    }

    public void testAbstractHandlerWithEligibilityCheckAndRevertExceptionsAlso() {
        handler.checkEligibility = true
        gameParam.players = [PONE, PTWO]
        boolean revertCalled = false
        def eligibilityResult = new PlayerGameEligibilityResult(eligibility: PlayerGameEligibility.FreeGameUsed)
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.gameTracker = [
                getGameEligibility   : {
                    Player p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                        throw new IllegalAccessException()
                }
        ] as PlayerGameTracker
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        throw new IllegalArgumentException()
                }
        ] as GamePhaseTransitionEngine

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail('should have exceptioned')
        } catch (IllegalArgumentException e) {
            assert revertCalled
        } catch (IllegalAccessException e) {
            fail('Should have caught and discarded IllegalAccessException')
        }
    }

    public void testAbstractHandlerCantLoadGame() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return null
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail("should have failed")
        } catch (FailedToFindGameException e) {

        }
    }


    public void testAbstractHandlerInvalidPlayer() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PTHREE.id
                        return PTHREE
                }
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleAction(PTHREE.id, gameId, testParam)
            fail("should have failed")
        } catch (PlayerNotPartOfGameException e) {
            //
        }
    }


    public void testAbstractHandlerBaseRotatesTurn() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Playing
        gameParam.gamePhase = GamePhase.Playing

        Game saved = gameParam.clone()
        Game transitioned = gameParam.clone();
        Game published = gameParam.clone()
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PTWO.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }

    public void testAbstractHandlerBaseRotatesTurnPastWordPhraseSetter() {
        gameParam.players = [PTWO, PONE, PTHREE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        gameParam.wordPhraseSetter = PTHREE.id
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Playing
        gameParam.gamePhase = GamePhase.Playing

        handledGame = (Game) gameParam.clone()
        Game saved = (Game) gameParam.clone()
        Game transitioned = (Game) gameParam.clone()
        Game published = (Game) gameParam.clone()
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PTWO.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }

    public void testAbstractHandlerBaseDoesNotRotateTurnWhenNotPlaying() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Setup
        gameParam.gamePhase = GamePhase.Setup

        Game saved = (Game) gameParam.clone()
        Game transitioned = (Game) gameParam.clone();
        Game published = (Game) gameParam.clone()
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PONE.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }


    public void testAbstractHandlerBaseDoesNotRotateTurnWhenPlayingNonTurnedBasedGame() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.remove(GameFeature.TurnBased)
        gameParam.featureData = [(GameFeature.TurnBased): PONE.id]
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Playing
        gameParam.gamePhase = GamePhase.Playing

        Game saved = (Game) gameParam.clone()
        Game transitioned = (Game) gameParam.clone();
        Game published = (Game) gameParam.clone()
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PONE.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }
}
