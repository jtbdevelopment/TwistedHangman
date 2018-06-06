package com.jtbdevelopment.TwistedHangman.game.handlers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.jtbdevelopment.games.exceptions.input.PlayerOutOfTurnException;
import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.Arrays;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

/**
 * Date: 12/22/14 Time: 7:46 AM
 */
public class AbstractGamePlayActionHandlerTest extends TwistedHangmanTestCase {

  private GameRepository gameRepository = mock(GameRepository.class);
  private MongoPlayerRepository playerRepository = mock(MongoPlayerRepository.class);
  private GamePhaseTransitionEngine transitionEngine = mock(GamePhaseTransitionEngine.class);
  private GamePublisher<Game, MongoPlayer> gamePublisher = mock(GamePublisher.class);
  private GameEligibilityTracker gameEligibilityTracker = mock(GameEligibilityTracker.class);
  private GameMasker gameMasker = mock(GameMasker.class);
  private TestHandler handler = new TestHandler(playerRepository, gameRepository, transitionEngine,
      gamePublisher, gameEligibilityTracker, gameMasker);
  private Game gameParam = new Game();
  private ObjectId gameId = new ObjectId();

  @Before
  public void setup() {
    when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameParam));
    when(playerRepository.findById(PONE.getId())).thenReturn(Optional.of(PONE));
    gameParam.setId(new ObjectId());
  }

  @Test
  public void testIgnoresNonTurnBasedGame() {
    Game saved = new Game();
    saved.setId(new ObjectId());
    Game transitioned = new Game();
    transitioned.setId(new ObjectId());
    Game published = new Game();
    published.setId(new ObjectId());
    gameParam.setPlayers(Arrays.asList(PONE, PTWO));
    when(gameRepository.save(transitioned)).thenReturn(saved);
    when(transitionEngine.evaluateGame(gameParam)).thenReturn(transitioned);
    when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    maskedGame.setId("X");
    when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);
    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, null));
  }

  @Test
  public void testOKForCorrectPlayerTurn() {
    Game saved = new Game();
    saved.setId(new ObjectId());
    Game transitioned = new Game();
    transitioned.setId(new ObjectId());
    Game published = new Game();
    published.setId(new ObjectId());
    gameParam.setPlayers(Arrays.asList(PONE, PTWO));
    gameParam.getFeatureData().put(GameFeature.TurnBased, PONE.getId());
    gameParam.getFeatures().add(GameFeature.TurnBased);
    when(gameRepository.save(transitioned)).thenReturn(saved);
    when(transitionEngine.evaluateGame(gameParam)).thenReturn(transitioned);
    when(gamePublisher.publish(saved, PONE)).thenReturn(published);
    MaskedGame maskedGame = new MaskedGame();
    maskedGame.setId("X");
    when(gameMasker.maskGameForPlayer(published, PONE)).thenReturn(maskedGame);
    assertSame(maskedGame, handler.handleAction(PONE.getId(), gameId, null));
  }

  @Test(expected = PlayerOutOfTurnException.class)
  public void testFailsForOutOfPlayerTurn() {
    gameParam.setPlayers(Arrays.asList(PONE, PTWO));
    gameParam.getFeatureData().put(GameFeature.TurnBased, PTWO.getId());
    gameParam.getFeatures().add(GameFeature.TurnBased);
    handler.handleAction(PONE.getId(), gameId, null);
  }

  public class TestHandler extends AbstractGamePlayActionHandler<Object> {

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
    protected Game handleActionInternal(MongoPlayer player, Game game, Object param) {
      return game;
    }

  }
}
