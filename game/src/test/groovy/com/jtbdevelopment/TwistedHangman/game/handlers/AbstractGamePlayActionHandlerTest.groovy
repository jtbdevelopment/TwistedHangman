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
import com.jtbdevelopment.games.exceptions.input.PlayerOutOfTurnException
import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.state.masking.GameMasker
import com.jtbdevelopment.games.state.transition.GameTransitionEngine
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import static org.junit.Assert.assertSame


/**
 * Date: 12/22/14
 * Time: 7:46 AM
 */
class AbstractGamePlayActionHandlerTest extends TwistedHangmanTestCase {
    class TestHandler extends AbstractGamePlayActionHandler<Object> {
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
        protected Game handleActionInternal(MongoPlayer player, Game game, Object param) {
            return game
        }

    }
    private GameRepository gameRepository = Mockito.mock(GameRepository.class)
    private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class)
    private GamePhaseTransitionEngine transitionEngine = Mockito.mock(GamePhaseTransitionEngine.class)
    private GamePublisher<Game, MongoPlayer> gamePublisher = Mockito.mock(GamePublisher.class)
    private GameEligibilityTracker gameEligibilityTracker = Mockito.mock(GameEligibilityTracker.class)
    private com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker gameMasker = Mockito.mock(com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker.class)
    private TestHandler handler = new TestHandler(playerRepository, gameRepository, transitionEngine, gamePublisher, gameEligibilityTracker, gameMasker)
    private Game gameParam = new Game()
    private ObjectId gameId = new ObjectId()

    @Before
    void setup() {
        Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameParam))
        Mockito.when(playerRepository.findById(PONE.id)).thenReturn(Optional.of(PONE))
        gameParam.setId(new ObjectId())
    }

    @Test
    void testIgnoresNonTurnBasedGame() {
        Game saved = new Game()
        saved.setId(new ObjectId())
        Game transitioned = new Game()
        transitioned.setId(new ObjectId())
        Game published = new Game()
        published.setId(new ObjectId())
        gameParam.players = [PONE, PTWO]
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(gameParam)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        maskedGame.setId("X")
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)
        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, null))
    }

    @Test
    void testOKForCorrectPlayerTurn() {
        Game saved = new Game()
        saved.setId(new ObjectId())
        Game transitioned = new Game()
        transitioned.setId(new ObjectId())
        Game published = new Game()
        published.setId(new ObjectId())
        gameParam.players = [PONE, PTWO]
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        gameParam.features += GameFeature.TurnBased
        Mockito.when(gameRepository.save(transitioned)).thenReturn(saved)
        Mockito.when(transitionEngine.evaluateGame(gameParam)).thenReturn(transitioned)
        Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published)
        MaskedGame maskedGame = new MaskedGame()
        maskedGame.setId("X")
        Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame)
        assertSame(maskedGame, handler.handleAction(PONE.id, gameId, null))
    }

    @Test(expected = PlayerOutOfTurnException.class)
    void testFailsForOutOfPlayerTurn() {
        gameParam.players = [PONE, PTWO]
        gameParam.featureData[GameFeature.TurnBased] = PTWO.id
        gameParam.features += GameFeature.TurnBased
        handler.handleAction(PONE.id, gameId, null)
    }
}
