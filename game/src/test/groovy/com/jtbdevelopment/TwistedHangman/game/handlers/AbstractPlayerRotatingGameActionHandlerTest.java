package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.dao.GameRepository;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine;
import com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/10/14 Time: 7:06 PM
 */
public class AbstractPlayerRotatingGameActionHandlerTest extends TwistedHangmanTestCase {

  private static final String testParam = "TESTPARAM";
  private final Game gameParam = new Game();
  private final ObjectId gameId = new ObjectId();
  private Game handledGame = new Game();
  private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class);
  private GameRepository gameRepository = Mockito.mock(GameRepository.class);
  private GamePhaseTransitionEngine transitionEngine = Mockito
      .mock(GamePhaseTransitionEngine.class);
  private GamePublisher gamePublisher = Mockito.mock(GamePublisher.class);
  private GameEligibilityTracker eligibilityTracker = Mockito.mock(GameEligibilityTracker.class);
  private GameMasker gameMasker = Mockito.mock(GameMasker.class);
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
    Game saved = new Game();
    saved.setId(new ObjectId());
    Game transitioned = new Game();
    transitioned.setId(new ObjectId());
    Game published = new Game();
    published.setId(new ObjectId());
    gameParam.setPlayers(Arrays.asList(PONE, PTWO));
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    maskedGame.setId(gameParam.getId().toHexString());
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);
    Assert.assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
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

    Game saved = makeSimpleGame();
    Game transitioned = makeSimpleGame();
    Game published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    Assert.assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    Assert.assertEquals(PTWO.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
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
    Game saved = makeSimpleGame();
    Game transitioned = makeSimpleGame();
    Game published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    Assert.assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
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

    Game saved = makeSimpleGame();
    Game transitioned = makeSimpleGame();
    Game published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    Assert.assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    Assert.assertEquals(PONE.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
  }

  @Test
  public void testAbstractHandlerBaseDoesNotRotateTurnWhenPlayingNonTurnedBasedGame() {
    gameParam.setPlayers(Arrays.asList(PTWO, PONE));
    gameParam.getFeatures().remove(GameFeature.TurnBased);
    LinkedHashMap<GameFeature, Object> map = new LinkedHashMap<>(1);
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

    Game saved = makeSimpleGame();
    Game transitioned = makeSimpleGame();
    Game published = makeSimpleGame();
    Mockito.when(gameRepository.save(transitioned)).thenReturn(saved);
    Mockito.when(transitionEngine.evaluateGame(handledGame)).thenReturn(transitioned);
    Mockito.when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    Mockito.when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);

    Assert.assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, testParam));
    Assert.assertEquals(PONE.getId(), handledGame.getFeatureData().get(GameFeature.TurnBased));
  }

  private class TestHandler extends AbstractPlayerRotatingGameActionHandler<String> {

    private boolean checkEligibility = false;
    private boolean internalException = false;

    public TestHandler(
        AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
        AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
        GameTransitionEngine<Game> transitionEngine, GamePublisher<Game, MongoPlayer> gamePublisher,
        GameEligibilityTracker gameTracker,
        com.jtbdevelopment.games.state.masking.GameMasker<ObjectId, Game, MaskedGame> gameMasker) {
      super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
          gameMasker);
    }

    @Override
    protected boolean requiresEligibilityCheck(final String param) {
      return checkEligibility;
    }

    @Override
    protected Game handleActionInternal(MongoPlayer player, Game game, String param) {
      assert param.equals(testParam);
      assert DefaultGroovyMethods.is(gameParam, game);
      if (internalException) {
        throw new IllegalStateException();
      }

      return handledGame;
    }
  }
}
