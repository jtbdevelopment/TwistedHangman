package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import com.jtbdevelopment.TwistedHangman.game.utility.RandomCannedGameFinder;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/6/14 Time: 9:13 PM
 */
public class SystemPuzzleSetterInitializerTest extends TwistedHangmanTestCase {

  private static final String phrase = "WHO LET THE DOGS OUT?";
  private static final String category = "SONG";
  private static final PreMadePuzzle cannedGame = new PreMadePuzzle() {{
    setWordPhrase(phrase);
    setCategory(category);
  }};
  private RandomCannedGameFinder cannedGameFinder = mock(RandomCannedGameFinder.class);
  private PhraseSetter phraseSetter = mock(PhraseSetter.class);
  private SystemPuzzleSetterInitializer puzzlerSetter = new SystemPuzzleSetterInitializer(
      cannedGameFinder, phraseSetter);

  @Before
  public void setUp() {
    Mockito.when(cannedGameFinder.getRandomGame()).thenReturn(cannedGame);
  }

  @Test
  public void testOrder() {
    assertEquals((GameInitializer.LATE_ORDER + 100), puzzlerSetter.getOrder());
  }

  @Test
  public void testSystemPuzzler() {
    Game game = new Game();
    game.getFeatures().add(GameFeature.SystemPuzzles);

    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);
    puzzlerSetter.initializeGame(game);

    game.getSolverStates().values()
        .forEach(state -> verify(phraseSetter).setWordPhrase(state, phrase, category));
  }

  @Test
  public void testNonSystemPuzzler() {
    Game game = new Game();
    Map<ObjectId, IndividualGameState> map = new HashMap<>();
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    map.put(new ObjectId(), new IndividualGameState(game.getFeatures()));
    game.setSolverStates(map);
    puzzlerSetter.initializeGame(game);
    game.getSolverStates().values()
        .forEach(state -> verify(phraseSetter, never()).setWordPhrase(state, phrase, category));
  }
}
