package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.masking.GameMasker
import com.jtbdevelopment.games.state.transition.GameTransitionEngine
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertSame

/**
 * Date: 11/10/14
 * Time: 7:06 PM
 */
class AbstractPlayerRotatingGameActionHandlerTest extends TwistedHangmanTestCase {
    private static final String testParam = "TESTPARAM"
    private Game handledGame = new Game()
    private final Game gameParam = new Game()
    private final ObjectId gameId = new ObjectId()

    private class TestHandler extends AbstractPlayerRotatingGameActionHandler<String> {
        boolean checkEligibility = false
        boolean internalException = false

        TestHandler(
                AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
                AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
                GameTransitionEngine<Game> transitionEngine,
                GamePublisher<Game, MongoPlayer> gamePublisher,
                GameEligibilityTracker gameTracker,
                GameMasker<ObjectId, Game, MaskedGame> gameMasker) {
            super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker, gameMasker)
        }

        @Override
        protected boolean requiresEligibilityCheck(final String param) {
            return checkEligibility
        }

        @Override
        protected Game handleActionInternal(MongoPlayer player, Game game, String param) {
            assert param == testParam
            assert gameParam.is(game)
            if (internalException) {
                throw new IllegalStateException()
            }
            return handledGame
        }
    }
    private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class)
    private GameRepository gameRepository = Mockito.mock(GameRepository.class)
    private GamePhaseTransitionEngine transitionEngine = Mockito.mock(GamePhaseTransitionEngine.class)
    private GamePublisher gamePublisher = Mockito.mock(GamePublisher.class)
    private GameEligibilityTracker eligibilityTracker = Mockito.mock(GameEligibilityTracker.class)
    private com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker gameMasker = Mockito.mock(com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker.class)
    private TestHandler handler = new TestHandler(playerRepository, gameRepository, transitionEngine, gamePublisher, eligibilityTracker, gameMasker)

    @Before
    void setup() {
        Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameParam))
        Mockito.when(playerRepository.findById(PONE.id)).thenReturn(Optional.of(PONE))
        gameParam.setId(new ObjectId())
        handledGame.setId(new ObjectId())
    }

    @Test
    void testAbstractHandlerBasic() {
        Game saved = new Game()
        saved.setId(new ObjectId())
        Game transitioned = new Game()
        transitioned.setId(new ObjectId())
        Game published = new Game()
        published.setId(new ObjectId())
        gameParam.players = [PONE, PTWO]
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        maskedGame.setId(gameParam.id.toHexString())
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)
        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, testParam))
    }

    @Test
    void testAbstractHandlerBaseRotatesTurn() {
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
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)

        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, testParam))
        assertEquals(PTWO.id, handledGame.featureData[GameFeature.TurnBased])
    }

    @Test
    void testAbstractHandlerBaseRotatesTurnPastWordPhraseSetter() {
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
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)

        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, testParam))
        assertEquals(PTWO.id, handledGame.featureData[GameFeature.TurnBased])
    }

    @Test
    void testAbstractHandlerBaseDoesNotRotateTurnWhenNotPlaying() {
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
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)

        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, testParam))
        assertEquals(PONE.id, handledGame.featureData[GameFeature.TurnBased])
    }

    @Test
    void testAbstractHandlerBaseDoesNotRotateTurnWhenPlayingNonTurnedBasedGame() {
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
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)

        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, testParam))
        assertEquals(PONE.id, handledGame.featureData[GameFeature.TurnBased])
    }
}
