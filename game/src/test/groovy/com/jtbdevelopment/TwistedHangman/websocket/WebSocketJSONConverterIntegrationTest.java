package com.jtbdevelopment.TwistedHangman.websocket;

import static groovy.util.GroovyTestCase.assertEquals;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedIndividualGameState;
import com.jtbdevelopment.TwistedHangman.json.TwistedHangmanJacksonRegistration;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.core.spring.jackson.ObjectMapperFactory;
import com.jtbdevelopment.games.mongo.json.MongoPlayerJacksonRegistration;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.PlayerState;
import com.jtbdevelopment.games.websocket.WebSocketJSONConverter;
import com.jtbdevelopment.games.websocket.WebSocketMessage;
import com.jtbdevelopment.games.websocket.WebSocketMessage.MessageType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * Date: 12/9/14 Time: 11:47 AM
 */
public class WebSocketJSONConverterIntegrationTest extends TwistedHangmanTestCase {

  private WebSocketJSONConverter webSocketJsonConverter = new WebSocketJSONConverter();
  private MaskedGame maskedGame = new MaskedGame() {{
    Map<GameFeature, Object> featureData = new HashMap<>(1);
    featureData.put(GameFeature.DrawFace, "A String");

    Map<String, String> players = new HashMap<>();
    players.put(PONE.getMd5(), PONE.getDisplayName());

    Map<String, Integer> roundScores = new HashMap<>();
    roundScores.put(PONE.getMd5(), 10);

    Map<String, Integer> runningScores = new HashMap<>();
    runningScores.put(PONE.getMd5(), 13);

    Map<String, PlayerState> playerStates = new HashMap<>();
    playerStates.put(PONE.getMd5(), PlayerState.Accepted);

    Map<String, MaskedIndividualGameState> solverStates = new HashMap<>();
    MaskedIndividualGameState state = new MaskedIndividualGameState();
    state.badlyGuessedLetters = new TreeSet<>(Arrays.asList('A', 'B'));
    state.blanksRemaining = 10;
    state.category = "Test";
    state.features = Sets.newHashSet(GameFeature.AllComplete);
    Map<GameFeature, Object> stateFeatureData = new HashMap<>();
    stateFeatureData.put(GameFeature.Thieving, 10);
    stateFeatureData.put(GameFeature.DrawGallows, "X");
    state.featureData = stateFeatureData;
    state.guessedLetters = new TreeSet<>(Collections.singletonList('B'));
    state.maxPenalties = 13;
    state.moveCount = 4;
    state.isPuzzleOver = false;
    state.isPuzzleSolved = false;
    state.isPlayerHung = true;
    state.penalties = 3;
    state.penaltiesRemaining = 5;
    state.workingWordPhrase = "____";
    state.wordPhrase = "555";
    solverStates.put(PONE.getMd5(), state);

    setCompletedTimestamp(100L);
    setCreated(1000L);
    setFeatureData(featureData);
    setFeatures(Sets.newHashSet(GameFeature.DrawFace, GameFeature.TurnBased));
    setGamePhase(GamePhase.Setup);
    setId("XYZ");
    setLastUpdate(5000L);
    setMaskedForPlayerID(PONE.getId().toHexString());
    setMaskedForPlayerMD5(PONE.getMd5());
    setPlayers(players);
    setPlayerRoundScores(roundScores);
    setPlayerRunningScores(runningScores);
    setPlayerStates(playerStates);
    setRound(10);
    setSolverStates(solverStates);
    setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_MD5);
  }};
  private String expectedString = "{\"messageType\":\"Game\",\"game\":{\"id\":\"XYZ\",\"previousId\":null,\"version\":null,\"round\":10,\"created\":1000,\"lastUpdate\":5000,\"completedTimestamp\":100,\"gamePhase\":\"Setup\",\"players\":{\"196c643ff2d27ff53cbd574c08c7726f\":\"100000000000000000000000\"},\"playerImages\":{},\"playerProfiles\":{},\"features\":[\"DrawFace\",\"TurnBased\"],\"featureData\":{\"DrawFace\":\"A String\"},\"maskedForPlayerID\":\"100000000000000000000000\",\"maskedForPlayerMD5\":\"196c643ff2d27ff53cbd574c08c7726f\",\"declinedTimestamp\":null,\"rematchTimestamp\":null,\"initiatingPlayer\":null,\"playerStates\":{\"196c643ff2d27ff53cbd574c08c7726f\":\"Accepted\"},\"wordPhraseSetter\":\"eb3e279a50b4c330f8d4a9e2abc678fe\",\"solverStates\":{\"196c643ff2d27ff53cbd574c08c7726f\":{\"wordPhrase\":\"555\",\"workingWordPhrase\":\"____\",\"badlyGuessedLetters\":[\"A\",\"B\"],\"guessedLetters\":[\"B\"],\"featureData\":{\"DrawGallows\":\"X\",\"Thieving\":10},\"category\":\"Test\",\"maxPenalties\":13,\"moveCount\":4,\"penalties\":3,\"blanksRemaining\":10,\"features\":[\"AllComplete\"],\"isPuzzleSolved\":false,\"isPlayerHung\":true,\"isPuzzleOver\":false,\"penaltiesRemaining\":5}},\"playerRoundScores\":{\"196c643ff2d27ff53cbd574c08c7726f\":10},\"playerRunningScores\":{\"196c643ff2d27ff53cbd574c08c7726f\":13},\"allPlayers\":null},\"player\":null,\"message\":null}";

  @Before
  public void setUp() throws Exception {
    ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory(
        Collections.emptyList(),
        Collections.emptyList(),
        Arrays.asList(
            new TwistedHangmanJacksonRegistration(),
            new MongoPlayerJacksonRegistration()));
    webSocketJsonConverter.setMapper(objectMapperFactory.getObject());
  }

  @Test
  public void testToJson() {
    WebSocketMessage message = new WebSocketMessage();
    message.setMessageType(MessageType.Game);
    message.setGame(maskedGame);
    String encoded = webSocketJsonConverter.encode(message);
    assertEquals(expectedString, encoded);
  }

  @Test
  public void testFromJson() {
    WebSocketMessage message = webSocketJsonConverter.decode(expectedString);
    assert MessageType.Game.equals(message.getMessageType());
    assert message.getGame() instanceof MaskedGame;
    //  Selected comparisons
    assert ((MaskedGame) message.getGame()).getSolverStates().size() == maskedGame
        .getSolverStates().size();
    assert DefaultGroovyMethods
        .equals(((MaskedGame) message.getGame()).getSolverStates().keySet(),
            maskedGame.getSolverStates().keySet());
    MaskedIndividualGameState poneState = ((MaskedGame) message.getGame())
        .getSolverStates().get(PONE.getMd5());
    assert poneState.featureData
        .equals(maskedGame.getSolverStates().get(PONE.getMd5()).featureData);
    assert poneState.features.equals(maskedGame.getSolverStates().get(PONE.getMd5()).features);
    assert poneState.badlyGuessedLetters
        .equals(maskedGame.getSolverStates().get(PONE.getMd5()).badlyGuessedLetters);
    assertEquals(maskedGame.getFeatures(), message.getGame().getFeatures());
    assertEquals(maskedGame.getFeatureData(), message.getGame().getFeatureData());
    assertEquals(maskedGame.getMaskedForPlayerMD5(),
        ((MaskedGame) message.getGame()).getMaskedForPlayerMD5());
    assertEquals(maskedGame.getPlayerStates(), ((MaskedGame) message.getGame()).getPlayerStates());
    assertEquals(maskedGame.getPlayerRunningScores(),
        ((MaskedGame) message.getGame()).getPlayerRunningScores());
    assertEquals(maskedGame.getPlayerRoundScores(),
        ((MaskedGame) message.getGame()).getPlayerRoundScores());

  }
}
