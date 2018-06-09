package com.jtbdevelopment.TwistedHangman.game.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.dao.GameRepository;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THGameMasker;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/10/14 Time: 7:06 PM
 */
public class AbstractPlayerRotatingGameActionHandlerTest extends TwistedHangmanTestCase {

  private static final String testParam = "TESTPARAM";
  private final THGame gameParam = new THGame();
  private final ObjectId gameId = new ObjectId();
  private THGame handledGame = new THGame();
  private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class);
  private GameRepository gameRepository = Mockito.mock(GameRepository.class);
  private GamePhaseTransitionEngine transitionEngine = Mockito
      .mock(GamePhaseTransitionEngine.class);
  private GamePublisher gamePublisher = Mockito.mock(GamePublisher.class);
  private GameEligibilityTracker eligibilityTracker = Mockito.mock(GameEligibilityTracker.class);
  private THGameMasker gameMasker = Mockito.mock(THGameMasker.class);
  private TestHandler handler = new TestHandler(playerRepository, gameRepository, transitionEngine,
      gamePublisher, eligibilityTracker, gameMasker);

  @Before
  public void setup() {
    Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameParam));
    Mockito.when(playerRepository.findById(PONE.getId())).thenReturn(Optional.of(PONE));
    gameParam.setId(new ObjectId());
    handledGame.setId(new ObjectId());
  }

  @Test
  public void testAbstractHandlerBasic() {
    THGame saved = new THGame();
    saved.setId(new ObjectId());
    THGame transitioned = new THGame();
    transitioned.setId(new ObjectId());
    THGame published = new THGame();
    published.setId(new ObjectId());
    gameParam.setPlayers(Arrays.asList(PONE, PTWO));
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    THMaskedGame maskedGame = new THMaskedGame();
    maskedGame.setId(gameParam.getId().toHexString());
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);
    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
  }

  @Test
  public void testAbstractHandlerBaseRotatesTurn() {
    gameParam.setPlayers(Arrays.asList(PTWO, PONE));
    gameParam.getFeatures().add(GameFeature.TurnBased);
    gameParam.getFeatureData().put(GameFeature.TurnBased, PONE.getId());
    handledGame.getFeatureData().clear();
    handledGame.getFeatureData().putAll(gameParam.getFeatureData());
    handledGame.getFeatures().clear();
    handledGame.getFeatures().addAll(gameParam.getFeatures());
    handledGame.getPlayers().clear();
    handledGame.getPlayers().addAll(gameParam.getPlayers());
    handledGame.setGamePhase(GamePhase.Playing);
    gameParam.setGamePhase(GamePhase.Playing);

    THGame saved = makeSimpleGame();
    THGame transitioned = makeSimpleGame();
    THGame published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    THMaskedGame maskedGame = new THMaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    assertEquals(PTWO.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
  }

  @Test
  public void testAbstractHandlerBaseRotatesTurnPastWordPhraseSetter() {
    gameParam.setPlayers(Arrays.asList(PTWO, PONE, PTHREE));
    gameParam.getFeatures().add(GameFeature.TurnBased);
    gameParam.getFeatureData().put(GameFeature.TurnBased, PONE.getId());
    gameParam.setWordPhraseSetter(PTHREE.getId());
    handledGame.getFeatureData().clear();
    handledGame.getFeatureData().putAll(gameParam.getFeatureData());
    handledGame.getFeatures().clear();
    handledGame.getFeatures().addAll(gameParam.getFeatures());
    handledGame.getPlayers().clear();
    handledGame.getPlayers().addAll(gameParam.getPlayers());
    handledGame.setGamePhase(GamePhase.Playing);
    gameParam.setGamePhase(GamePhase.Playing);

    handledGame = makeSimpleGame();
    THGame saved = makeSimpleGame();
    THGame transitioned = makeSimpleGame();
    THGame published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    THMaskedGame maskedGame = new THMaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
  }

  @Test
  public void testAbstractHandlerBaseDoesNotRotateTurnWhenNotPlaying() {
    gameParam.setPlayers(Arrays.asList(PTWO, PONE));
    gameParam.getFeatures().add(GameFeature.TurnBased);
    gameParam.getFeatureData().put(GameFeature.TurnBased, PONE.getId());
    handledGame.getFeatureData().clear();
    handledGame.getFeatureData().putAll(gameParam.getFeatureData());
    handledGame.getFeatures().clear();
    handledGame.getFeatures().addAll(gameParam.getFeatures());
    handledGame.getPlayers().clear();
    handledGame.getPlayers().addAll(gameParam.getPlayers());
    handledGame.setGamePhase(GamePhase.Setup);
    gameParam.setGamePhase(GamePhase.Setup);

    THGame saved = makeSimpleGame();
    THGame transitioned = makeSimpleGame();
    THGame published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    THMaskedGame maskedGame = new THMaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    assertEquals(PONE.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
  }

  @Test
  public void testAbstractHandlerBaseDoesNotRotateTurnWhenPlayingNonTurnedBasedGame() {
    gameParam.setPlayers(Arrays.asList(PTWO, PONE));
    gameParam.getFeatures().remove(GameFeature.TurnBased);
    Map<GameFeature, Object> map = new HashMap<>();
    map.put(GameFeature.TurnBased, PONE.getId());
    gameParam.setFeatureData(map);
    handledGame.getFeatureData().clear();
    handledGame.getFeatureData().putAll(gameParam.getFeatureData());
    handledGame.getFeatures().clear();
    handledGame.getFeatures().addAll(gameParam.getFeatures());
    handledGame.getPlayers().clear();
    handledGame.getPlayers().addAll(gameParam.getPlayers());
    handledGame.setGamePhase(GamePhase.Playing);
    gameParam.setGamePhase(GamePhase.Playing);

    THGame saved = makeSimpleGame();
    THGame transitioned = makeSimpleGame();
    THGame published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    THMaskedGame maskedGame = new THMaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    assertEquals(PONE.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
  }

  private class TestHandler extends AbstractPlayerRotatingGameActionHandler<String> {

    private boolean checkEligibility = false;
    private boolean internalException = false;

    public TestHandler(
        AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
        AbstractGameRepository<ObjectId, GameFeature, THGame> gameRepository,
        GameTransitionEngine<THGame> transitionEngine,
        GamePublisher<THGame, MongoPlayer> gamePublisher,
        GameEligibilityTracker gameTracker,
        com.jtbdevelopment.games.state.masking.GameMasker<ObjectId, THGame, THMaskedGame> gameMasker) {
      super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
          gameMasker);
    }

    @Override
    protected boolean requiresEligibilityCheck(final String param) {
      return checkEligibility;
    }

    @Override
    protected THGame handleActionInternal(MongoPlayer player, THGame game, String param) {
      assertEquals(testParam, param);
      assertSame(game, gameParam);
      if (internalException) {
        throw new IllegalStateException();
      }

      return handledGame;
    }
  }
}
