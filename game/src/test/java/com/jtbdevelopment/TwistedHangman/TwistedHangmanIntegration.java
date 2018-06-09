package com.jtbdevelopment.TwistedHangman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.dao.GameRepository;
import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices.FeaturesAndPlayers;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.PlayerState;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Date: 11/15/2014 Time: 3:29 PM
 */
public class TwistedHangmanIntegration extends AbstractGameIntegration<THGame, THMaskedGame> {

  private static final String LOREMIPSUM = "Lorem ipsum";
  private static GameRepository gameRepository;

  @BeforeClass
  public static void createPuzzle() {
    PreMadePuzzle puzzle = new PreMadePuzzle();
    puzzle.setCategory("PHRASE");
    puzzle.setWordPhrase(LOREMIPSUM);
    puzzle.setSource("test");
    applicationContext.getBean(PreMadePuzzleRepository.class).save(puzzle);
    gameRepository = applicationContext.getBean(GameRepository.class);
  }

  private static THMaskedGame putMG(final WebTarget webTarget) {
    return webTarget.request(MediaType.APPLICATION_JSON).put(EMPTY_PUT_POST, THMaskedGame.class);
  }

  @Override
  public Class<THMaskedGame> returnedGameClass() {
    return THMaskedGame.class;
  }

  public Class<THGame> internalGameClass() {
    return THGame.class;
  }

  public THGame newGame() {
    return new THGame();
  }

  public AbstractGameRepository gameRepository() {
    return gameRepository;
  }

  @Test
  public void testGetFeatures() {
    WebTarget client = AbstractGameIntegration.createAPITarget(TEST_PLAYER2);
    Map<String, String> features = client.path("features").request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Map<String, String>>() {
        });
    LinkedHashMap<String, String> map = new LinkedHashMap<>(10);
    map.put("Thieving", GameFeature.Thieving.getDescription());
    map.put("Live", GameFeature.Live.getDescription());
    map.put("TurnBased", GameFeature.TurnBased.getDescription());
    map.put("Head2Head", GameFeature.Head2Head.getDescription());
    map.put("SystemPuzzles", GameFeature.SystemPuzzles.getDescription());
    map.put("AlternatingPuzzleSetter", GameFeature.AlternatingPuzzleSetter.getDescription());
    map.put("AllComplete", GameFeature.AllComplete.getDescription());
    map.put("SingleWinner", GameFeature.SingleWinner.getDescription());
    map.put("DrawGallows", GameFeature.DrawGallows.getDescription());
    map.put("DrawFace", GameFeature.DrawFace.getDescription());
    assertEquals(map, features);
  }

  @Test
  public void testQuittingAGame() {
    FeaturesAndPlayers players = new FeaturesAndPlayers();
    players.setFeatures(Sets.newHashSet(GameFeature.SystemPuzzles, GameFeature.Thieving,
        GameFeature.DrawFace,
        GameFeature.SingleWinner, GameFeature.Live));
    players.setPlayers(
        Arrays.asList(TEST_PLAYER2.getMd5(), TEST_PLAYER3.getMd5()));
    Entity<FeaturesAndPlayers> entity = Entity.entity(players, MediaType.APPLICATION_JSON);

    WebTarget P1 = AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER1);

    THMaskedGame game;
    game = P1.path("new").request(MediaType.APPLICATION_JSON).post(entity, THMaskedGame.class);

    assertEquals(GamePhase.Challenged, game.getGamePhase());
    assertNotNull(game.getSolverStates().get(TEST_PLAYER1.getMd5()));
    Assert.assertNotEquals("", game.getSolverStates().get(TEST_PLAYER1.getMd5()).workingWordPhrase);
    WebTarget P1G = AbstractGameIntegration.createGameTarget(P1, game);

    game = quitGame(P1G);
    assertEquals(GamePhase.Quit, game.getGamePhase());
    assertEquals(PlayerState.Quit, game.getPlayerStates().get(TEST_PLAYER1.getMd5()));
  }

  @Test
  public void testPlayingAMultiPlayerGame() {
    FeaturesAndPlayers players = new FeaturesAndPlayers();
    players.setFeatures(Sets.newHashSet(GameFeature.SystemPuzzles, GameFeature.Thieving,
        GameFeature.DrawFace,
        GameFeature.SingleWinner, GameFeature.Live));
    players.setPlayers(Arrays.asList(TEST_PLAYER2.getMd5(), TEST_PLAYER3.getMd5()));
    Entity<FeaturesAndPlayers> entity = Entity.entity(players,
        MediaType.APPLICATION_JSON);

    WebTarget P1 = AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER1);

    THMaskedGame game = P1.path("new").request(MediaType.APPLICATION_JSON)
        .post(entity, THMaskedGame.class);

    assertEquals(GamePhase.Challenged, game.getGamePhase());
    assertNotNull(game.getSolverStates().get(TEST_PLAYER1.getMd5()));
    assertEquals("_____ _____",
        game.getSolverStates().get(TEST_PLAYER1.getMd5()).workingWordPhrase);
    WebTarget P1G = AbstractGameIntegration.createGameTarget(P1, game);
    WebTarget P2G = AbstractGameIntegration
        .createGameTarget(AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER2), game);
    final WebTarget P3G = AbstractGameIntegration
        .createGameTarget(AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3), game);

    game = acceptGame(P2G);
    game = getGame(P2G);
    assertEquals(GamePhase.Challenged, game.getGamePhase());
    assertNotNull(game.getSolverStates().get(TEST_PLAYER2.getMd5()));
    assertEquals("_____ _____",
        game.getSolverStates().get(TEST_PLAYER2.getMd5()).workingWordPhrase);
    game = acceptGame(P3G);
    game = getGame(P3G);
    assertEquals(GamePhase.Playing, game.getGamePhase());
    assertNotNull(game.getSolverStates().get(TEST_PLAYER3.getMd5()));
    assertEquals("_____ _____",
        game.getSolverStates().get(TEST_PLAYER3.getMd5()).workingWordPhrase);

    GameRepository gameRepository = applicationContext.getBean(GameRepository.class);
    THGame dbLoaded1 = gameRepository.findById(new ObjectId(game.getIdAsString())).get();

    char[] phraseArray = LOREMIPSUM.toCharArray();
    int position = 0;
    while (!Character.isAlphabetic(phraseArray[0])) {
      ++position;
    }

    game = putMG(P1G.path("steal").path("0"));
    assertEquals(GamePhase.Playing, game.getGamePhase());
    assertEquals(1, game.getSolverStates().get(TEST_PLAYER1.getMd5()).penalties);
    assertEquals("", game.getSolverStates().get(TEST_PLAYER1.getMd5()).wordPhrase);
    assertEquals(9,
        game.getSolverStates().get(TEST_PLAYER1.getMd5()).penaltiesRemaining);
    assertEquals(1, game.getSolverStates().get(TEST_PLAYER1.getMd5()).featureData
        .get(GameFeature.ThievingCountTracking));
    assertEquals(Arrays.asList("L"),
        game.getSolverStates().get(TEST_PLAYER1.getMd5()).featureData
            .get(GameFeature.ThievingLetters));
    Assert.assertTrue(
        ((List<Boolean>) game.getSolverStates().get(TEST_PLAYER1.getMd5()).featureData
            .get(GameFeature.ThievingPositionTracking)).get(0));
    Assert.assertFalse(
        ((List<Boolean>) game.getSolverStates().get(TEST_PLAYER1.getMd5()).featureData
            .get(GameFeature.ThievingPositionTracking)).get(1));
    assertNotNull(game.getSolverStates().get(TEST_PLAYER3.getMd5()));
    assertEquals("", game.getSolverStates().get(TEST_PLAYER3.getMd5()).wordPhrase);

    Set<Character> chars = LOREMIPSUM.chars().filter(Character::isAlphabetic)
        .mapToObj(Character::toUpperCase).map(c -> (char) (int) c).collect(Collectors.toSet());
    Character letter = chars.iterator().next();
    game = putMG(P2G.path("guess").path(letter.toString()));
    assertEquals(GamePhase.Playing, game.getGamePhase());
    assertEquals(
        Sets.newHashSet(letter),
        game.getSolverStates().get(TEST_PLAYER2.getMd5()).guessedLetters);

    THMaskedGame[] gameRef = new THMaskedGame[1];
    chars.forEach(c -> gameRef[0] = putMG(P3G.path("guess").path(c.toString())));
    game = gameRef[0];
    assertEquals(GamePhase.RoundOver, game.getGamePhase());
    assertEquals(chars,
        game.getSolverStates().get(TEST_PLAYER3.getMd5()).guessedLetters);
    LinkedHashMap<String, Integer> map = new LinkedHashMap<>(3);
    map.put(TEST_PLAYER1.getMd5(), 0);
    map.put(TEST_PLAYER2.getMd5(), 0);
    map.put(TEST_PLAYER3.getMd5(), 1);
    assertEquals(map, game.getPlayerRunningScores());
    assertEquals(TEST_PLAYER3.getMd5(),
        game.getFeatureData().get(GameFeature.SingleWinner));

    THMaskedGame newGame = rematchGame(P2G);
    Assert.assertNotEquals(newGame.getId(), dbLoaded1.getId());
    THGame dbLoaded2 = gameRepository.findById(new ObjectId(newGame.getIdAsString())).get();
    dbLoaded1 = gameRepository.findById(dbLoaded1.getId()).get();
    assertEquals(dbLoaded1.getId(), dbLoaded2.getPreviousId());
    assertNotNull(dbLoaded1.getRematchTimestamp());
    assertEquals(GamePhase.Challenged, newGame.getGamePhase());
    assertEquals(game.getPlayers(), newGame.getPlayers());
    assertEquals(game.getPlayerRunningScores(), newGame.getPlayerRunningScores());
    assertEquals(2, newGame.getRound());

    newGame = rejectGame(AbstractGameIntegration.createGameTarget(P1, newGame));
    assertEquals(GamePhase.Declined, newGame.getGamePhase());
  }
}
