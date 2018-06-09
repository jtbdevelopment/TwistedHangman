package com.jtbdevelopment.TwistedHangman.game.state.masked;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedIndividualGameState;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.PlayerState;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeSet;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/14/14 Time: 9:15 PM
 */
public class GameMaskerTest extends TwistedHangmanTestCase {

  private GameMasker masker = new GameMasker();

  @Test
  public void testMaskingSinglePlayerGame() {
    IndividualGameState state = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map = new LinkedHashMap<>(2);
    map.put(GameFeature.DrawGallows, PONE.getId());
    map.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setCategory("cat");
    state.setFeatureData(map);
    state.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setMaxPenalties(10);
    state.setMoveCount(2);
    state.setPenalties(2);
    state.setBlanksRemaining(4);
    state.setWorkingWordPhraseString("__'_");
    state.setWordPhraseString("SAY'S");
    Game game = new Game();

    LinkedHashMap<GameFeature, Object> featureData = new LinkedHashMap<>(1);
    featureData.put(GameFeature.DrawFace, "");

    LinkedHashMap<ObjectId, PlayerState> playerStates = new LinkedHashMap<>(1);
    playerStates.put(PONE.getId(), PlayerState.Accepted);

    LinkedHashMap<ObjectId, Integer> runningScores = new LinkedHashMap<>(1);
    runningScores.put(PONE.getId(), 5);

    LinkedHashMap<ObjectId, Integer> roundScores = new LinkedHashMap<>(1);
    roundScores.put(PONE.getId(), 0);

    LinkedHashMap<ObjectId, IndividualGameState> gameStates = new LinkedHashMap<>(
        1);
    gameStates.put(PONE.getId(), state);

    game.setGamePhase(GamePhase.Playing);
    game.setPlayers(new ArrayList<>(Collections.singletonList(PONE)));
    game.setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId());
    game.setCreated(Instant.now());
    game.setCompletedTimestamp(Instant.now());
    game.setDeclinedTimestamp(Instant.now());
    game.setFeatureData(featureData);
    game.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.SystemPuzzles, GameFeature.SinglePlayer)));
    game.setId(new ObjectId());
    game.setInitiatingPlayer(PONE.getId());
    game.setLastUpdate(Instant.now());
    game.setPlayerStates(playerStates);
    game.setPlayerRunningScores(runningScores);
    game.setPlayerRoundScores(roundScores);
    game.setPreviousId(new ObjectId());
    game.setRematchTimestamp(Instant.now());
    game.setRound(new Random().nextInt(1000));
    game.setSolverStates(gameStates);
    game.setVersion(10);

    MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE);
    checkUnmaskedGameFields(maskedGame, game);

    LinkedHashMap<String, Integer> expectedRunningScores = new LinkedHashMap<>(1);
    expectedRunningScores.put(PONE.getMd5(), 5);
    assertEquals(expectedRunningScores, maskedGame.getPlayerRunningScores());
    LinkedHashMap<String, Integer> expectedScores = new LinkedHashMap<>(1);
    expectedScores.put(PONE.getMd5(), 0);
    assertEquals(expectedScores, maskedGame.getPlayerRoundScores());
    assertEquals(PONE.getId().toHexString(), maskedGame.getMaskedForPlayerID());
    assertEquals(PONE.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getMd5(),
        maskedGame.getWordPhraseSetter());
    assertEquals(game.getFeatureData(), maskedGame.getFeatureData());

    assertEquals(1, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PONE.getMd5()));
    MaskedIndividualGameState maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    LinkedHashMap<GameFeature, Object> expectedMaskedFeatureData = new LinkedHashMap<>(2);
    expectedMaskedFeatureData.put(GameFeature.DrawGallows, PONE.getMd5());
    expectedMaskedFeatureData
        .put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(expectedMaskedFeatureData, maskedState.featureData);
    assertEquals("", maskedState.wordPhrase);
    checkUnmaskedData(maskedState, state);

    //  Flip game over and check word phrase
    game.setGamePhase(GamePhase.RoundOver);
    maskedGame = masker.maskGameForPlayer(game, PONE);
    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    assertEquals(state.getWordPhraseString(), maskedState.wordPhrase);
  }

  @Test
  public void testMaskingTwoPlayerHeadToHead() {
    IndividualGameState state = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map = new LinkedHashMap<>(2);
    map.put(GameFeature.DrawGallows, PONE.getId());
    map.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setCategory("cat1");
    state.setFeatureData(map);
    state.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setMaxPenalties(10);
    state.setMoveCount(2);
    state.setBlanksRemaining(4);
    state.setPenalties(2);
    state.setWorkingWordPhraseString("__'_");
    state.setWordPhraseString("SAY'S");
    IndividualGameState state2 = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map1 = new LinkedHashMap<>(2);
    map1.put(GameFeature.DrawGallows, PTWO.getId());
    map1.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state2.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setCategory("cat2");
    state2.setFeatureData(map1);
    state2.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state2.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setMaxPenalties(10);
    state2.setMoveCount(4);
    state2.setPenalties(3);
    state2.setBlanksRemaining(3);
    state2.setWorkingWordPhraseString("__");
    state2.setWordPhraseString("SAY");
    Game game = new Game();

    LinkedHashMap<GameFeature, Object> featureData = new LinkedHashMap<>(
        2);
    featureData.put(GameFeature.DrawFace, "");
    featureData.put(GameFeature.SingleWinner, PTWO.getId());

    LinkedHashMap<ObjectId, PlayerState> map3 = new LinkedHashMap<>(2);
    map3.put(PONE.getId(), PlayerState.Accepted);
    map3.put(PTWO.getId(), PlayerState.Rejected);

    LinkedHashMap<ObjectId, Integer> map4 = new LinkedHashMap<>(2);
    map4.put(PONE.getId(), 5);
    map4.put(PTWO.getId(), 7);

    LinkedHashMap<ObjectId, Integer> map5 = new LinkedHashMap<>(2);
    map5.put(PONE.getId(), 1);
    map5.put(PTWO.getId(), 0);

    LinkedHashMap<ObjectId, IndividualGameState> map6 = new LinkedHashMap<>(
        2);
    map6.put(PONE.getId(), state);
    map6.put(PTWO.getId(), state2);

    game.setGamePhase(GamePhase.Playing);
    game.setPlayers(Arrays.asList(PONE, PTWO));
    game.setWordPhraseSetter(null);
    game.setCreated(Instant.now());
    game.setCompletedTimestamp(Instant.now());
    game.setDeclinedTimestamp(Instant.now());

    game.setFeatureData(featureData);
    game.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.SystemPuzzles, GameFeature.SinglePlayer)));
    game.setId(new ObjectId());
    game.setInitiatingPlayer(PTWO.getId());
    game.setLastUpdate(Instant.now());
    game.setPlayerStates(map3);
    game.setPlayerRunningScores(map4);
    game.setPlayerRoundScores(map5);
    game.setPreviousId(new ObjectId());
    game.setRematchTimestamp(Instant.now());
    game.setSolverStates(map6);
    game.setRound(new Random().nextInt(1000));
    game.setVersion(10);

    MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE);
    checkUnmaskedGameFields(maskedGame, game);

    LinkedHashMap<String, Integer> expectedRunningScores = new LinkedHashMap<>(2);
    expectedRunningScores.put(PONE.getMd5(), 5);
    expectedRunningScores.put(PTWO.getMd5(), 7);
    assertEquals(expectedRunningScores, maskedGame.getPlayerRunningScores());
    LinkedHashMap<String, Integer> expectedRoundScores = new LinkedHashMap<>(2);
    expectedRoundScores.put(PONE.getMd5(), 1);
    expectedRoundScores.put(PTWO.getMd5(), 0);
    assertEquals(expectedRoundScores, maskedGame.getPlayerRoundScores());
    assertEquals(PONE.getId().toHexString(), maskedGame.getMaskedForPlayerID());
    assertEquals(PONE.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertNull(maskedGame.getWordPhraseSetter());
    LinkedHashMap<GameFeature, String> expectedFeatureDAta = new LinkedHashMap<>(2);
    expectedFeatureDAta.put(GameFeature.DrawFace, "");
    expectedFeatureDAta.put(GameFeature.SingleWinner, PTWO.getMd5());
    assertEquals(expectedFeatureDAta, maskedGame.getFeatureData());

    assertEquals(2, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PONE.getMd5()));
    assertTrue(maskedGame.getSolverStates().containsKey(PTWO.getMd5()));

    MaskedIndividualGameState maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    LinkedHashMap<GameFeature, Object> expectedFeatureData = new LinkedHashMap<>(2);
    expectedFeatureData.put(GameFeature.DrawGallows, PONE.getMd5());
    expectedFeatureData
        .put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(expectedFeatureData, maskedState.featureData);
    assertEquals("", maskedState.wordPhrase);

    maskedState = maskedGame.getSolverStates().get(PTWO.getMd5());
    checkUnmaskedData(maskedState, state2);
    LinkedHashMap<GameFeature, Object> map15 = new LinkedHashMap<>(2);
    map15.put(GameFeature.DrawGallows, PTWO.getMd5());
    map15.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map15, maskedState.featureData);
    assertEquals(state2.getWordPhraseString(), maskedState.wordPhrase);
    assertEquals(state2.getBadlyGuessedLetters(), maskedState.badlyGuessedLetters);
    assertEquals(state2.getGuessedLetters(), maskedState.guessedLetters);

    //  Flip over
    game.setGamePhase(GamePhase.NextRoundStarted);
    maskedGame = masker.maskGameForPlayer(game, PONE);

    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    LinkedHashMap<GameFeature, Object> map16 = new LinkedHashMap<>(2);
    map16.put(GameFeature.DrawGallows, PONE.getMd5());
    map16.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map16, maskedState.featureData);
    assertEquals(state.getWordPhraseString(), maskedState.wordPhrase);

    maskedState = maskedGame.getSolverStates().get(PTWO.getMd5());
    checkUnmaskedData(maskedState, state2);
    LinkedHashMap<GameFeature, Object> map17 = new LinkedHashMap<>(2);
    map17.put(GameFeature.DrawGallows, PTWO.getMd5());
    map17.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map17, maskedState.featureData);
    assertEquals(state2.getWordPhraseString(), maskedState.wordPhrase);
    assertEquals(state2.getBadlyGuessedLetters(), maskedState.badlyGuessedLetters);
  }

  @Test
  public void testMaskingMultiPlayerSystemPuzzler() {
    IndividualGameState state = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map = new LinkedHashMap<>(2);
    map.put(GameFeature.DrawGallows, PONE.getId());
    map.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setCategory("cat1");
    state.setFeatureData(map);
    state.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setMaxPenalties(10);
    state.setMoveCount(2);
    state.setBlanksRemaining(4);
    state.setPenalties(2);
    state.setWorkingWordPhraseString("__'_");
    state.setWordPhraseString("SAY'S");
    IndividualGameState state2 = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map1 = new LinkedHashMap<>(2);
    map1.put(GameFeature.DrawGallows, PTWO.getId());
    map1.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state2.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setCategory("cat2");
    state2.setFeatureData(map1);
    state2.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state2.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setMaxPenalties(10);
    state2.setMoveCount(4);
    state2.setBlanksRemaining(3);
    state2.setPenalties(3);
    state2.setWorkingWordPhraseString("__");
    state2.setWordPhraseString("SAY");
    IndividualGameState state3 = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> featureData = new LinkedHashMap<>(2);
    featureData.put(GameFeature.DrawGallows, PTWO.getId());
    featureData.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state3.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state3.setCategory("cat3");
    state3.setFeatureData(featureData);
    state3.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state3.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state3.setMaxPenalties(10);
    state3.setMoveCount(4);
    state3.setPenalties(3);
    state3.setWorkingWordPhraseString("__");
    state3.setWordPhraseString("SAY");

    LinkedHashMap<ObjectId, IndividualGameState> states = new LinkedHashMap<>(
        3);
    states.put(PONE.getId(), state);
    states.put(PTWO.getId(), state2);
    states.put(PTHREE.getId(), state3);
    Game game = makeMultiPlayerGame(TwistedHangmanSystemPlayerCreator.TH_PLAYER, states);

    MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE);
    checkUnmaskedGameFields(maskedGame, game);
    checkMultiPlayerGame(maskedGame);
    assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getMd5(),
        maskedGame.getWordPhraseSetter());
    assertEquals(PONE.getId().toHexString(), maskedGame.getMaskedForPlayerID());
    assertEquals(PONE.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertEquals(3, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PONE.getMd5()));
    MaskedIndividualGameState maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    LinkedHashMap<GameFeature, Object> map4 = new LinkedHashMap<>(2);
    map4.put(GameFeature.DrawGallows, PONE.getMd5());
    map4.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map4, maskedState.featureData);
    assertEquals("", maskedState.wordPhrase);
    assertEquals(state.getBadlyGuessedLetters(), maskedState.badlyGuessedLetters);
    assertEquals(state.getGuessedLetters(), maskedState.guessedLetters);
    maskedState = maskedGame.getSolverStates().get(PTWO.getMd5());
    checkPartialMaskedData(maskedState, state2);
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkPartialMaskedData(maskedState, state3);

    maskedGame = masker.maskGameForPlayer(game, PTHREE);
    checkUnmaskedGameFields(maskedGame, game);
    checkMultiPlayerGame(maskedGame);
    assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getMd5(),
        maskedGame.getWordPhraseSetter());
    assertEquals(PTHREE.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertEquals(3, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PTHREE.getMd5()));
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkUnmaskedData(maskedState, state3);
    LinkedHashMap<GameFeature, Object> map5 = new LinkedHashMap<>(2);
    map5.put(GameFeature.DrawGallows, PTWO.getMd5());
    map5.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map5, maskedState.featureData);
    assertEquals("", maskedState.wordPhrase);
    maskedState = maskedGame.getSolverStates().get(PTWO.getMd5());
    checkPartialMaskedData(maskedState, state2);

    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkPartialMaskedData(maskedState, state);

    game.setGamePhase(GamePhase.NextRoundStarted);
    maskedGame = masker.maskGameForPlayer(game, PTHREE);
    assertEquals(3, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PTHREE.getMd5()));
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkUnmaskedData(maskedState, state3);
    LinkedHashMap<GameFeature, Object> map6 = new LinkedHashMap<>(2);
    map6.put(GameFeature.DrawGallows, PTWO.getMd5());
    map6.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map6, maskedState.featureData);
    assertEquals(state3.getWordPhraseString(), maskedState.wordPhrase);
    maskedState = maskedGame.getSolverStates().get(PTWO.getMd5());
    checkUnmaskedData(maskedState, state2);
    assertEquals(state2.getWordPhraseString(), maskedState.wordPhrase);
    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    assertEquals(state.getWordPhraseString(), maskedState.wordPhrase);
  }

  @Test
  public void testMaskingMultiPlayerNonSystemPuzzler() {
    IndividualGameState state = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map = new LinkedHashMap<>(2);
    map.put(GameFeature.DrawGallows, PONE.getId());
    map.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setCategory("cat1");
    state.setFeatureData(map);
    state.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state.setMaxPenalties(10);
    state.setMoveCount(2);
    state.setBlanksRemaining(4);
    state.setPenalties(2);
    state.setWorkingWordPhraseString("__'_");
    state.setWordPhraseString("SAY'S");
    IndividualGameState state2 = new IndividualGameState();

    LinkedHashMap<GameFeature, Object> map1 = new LinkedHashMap<>(2);
    map1.put(GameFeature.DrawGallows, PTWO.getId());
    map1.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));

    state2.setBadlyGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setCategory("cat3");
    state2.setFeatureData(map1);
    state2.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.TurnBased)));
    state2.setGuessedLetters(new TreeSet<>(Arrays.asList('A', 'B')));
    state2.setMaxPenalties(10);
    state2.setMoveCount(4);
    state2.setBlanksRemaining(3);
    state2.setPenalties(3);
    state2.setWorkingWordPhraseString("__");
    state2.setWordPhraseString("SAY");

    LinkedHashMap<ObjectId, IndividualGameState> states = new LinkedHashMap<>(
        2);
    states.put(PONE.getId(), state);
    states.put(PTHREE.getId(), state2);
    Game game = makeMultiPlayerGame(PTWO, states);

    MaskedGame maskedGame = masker.maskGameForPlayer(game, PTWO);
    checkUnmaskedGameFields(maskedGame, game);
    checkMultiPlayerGame(maskedGame);
    assertEquals(PTWO.getId().toHexString(), maskedGame.getMaskedForPlayerID());
    assertEquals(PTWO.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertEquals(2, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PONE.getMd5()));
    assertTrue(maskedGame.getSolverStates().containsKey(PTHREE.getMd5()));
    assertEquals(PTWO.getMd5(), maskedGame.getWordPhraseSetter());
    MaskedIndividualGameState maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    LinkedHashMap<GameFeature, Object> map3 = new LinkedHashMap<>(2);
    map3.put(GameFeature.DrawGallows, PONE.getMd5());
    map3.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map3, maskedState.featureData);
    assertEquals(state.getWordPhraseString(), maskedState.wordPhrase);
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkUnmaskedData(maskedState, state2);
    LinkedHashMap<GameFeature, Object> map4 = new LinkedHashMap<>(2);
    map4.put(GameFeature.DrawGallows, PTWO.getMd5());
    map4.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map4, maskedState.featureData);
    assertEquals(state2.getWordPhraseString(), maskedState.wordPhrase);

    maskedGame = masker.maskGameForPlayer(game, PTHREE);
    checkUnmaskedGameFields(maskedGame, game);
    checkMultiPlayerGame(maskedGame);
    assertEquals(PTHREE.getMd5(), maskedGame.getMaskedForPlayerMD5());
    assertEquals(2, maskedGame.getSolverStates().size());
    assertTrue(maskedGame.getSolverStates().containsKey(PTHREE.getMd5()));
    assertEquals(PTWO.getMd5(), maskedGame.getWordPhraseSetter());
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkUnmaskedData(maskedState, state2);
    LinkedHashMap<GameFeature, Object> map5 = new LinkedHashMap<>(2);
    map5.put(GameFeature.DrawGallows, PTWO.getMd5());
    map5.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map5, maskedState.featureData);
    assertEquals("", maskedState.wordPhrase);

    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkPartialMaskedData(maskedState, state);

    game.setGamePhase(GamePhase.RoundOver);
    maskedGame = masker.maskGameForPlayer(game, PTHREE);
    maskedState = maskedGame.getSolverStates().get(PTHREE.getMd5());
    checkUnmaskedData(maskedState, state2);
    LinkedHashMap<GameFeature, Object> map6 = new LinkedHashMap<>(2);
    map6.put(GameFeature.DrawGallows, PTWO.getMd5());
    map6.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map6, maskedState.featureData);
    assertEquals(state2.getWordPhraseString(), maskedState.wordPhrase);

    maskedState = maskedGame.getSolverStates().get(PONE.getMd5());
    checkUnmaskedData(maskedState, state);
    LinkedHashMap<GameFeature, Object> map7 = new LinkedHashMap<>(2);
    map7.put(GameFeature.DrawGallows, PONE.getMd5());
    map7.put(GameFeature.Thieving, new ArrayList<>(Arrays.asList(true, true, false)));
    assertEquals(map7, maskedState.featureData);
    assertEquals(state.getWordPhraseString(), maskedState.wordPhrase);
    assertEquals(state.getBadlyGuessedLetters(), maskedState.badlyGuessedLetters);
    assertEquals(state.getGuessedLetters(), maskedState.guessedLetters);
  }

  private void checkUnmaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
    assertEquals(state.getBlanksRemaining(), maskedState.blanksRemaining);
    assertEquals(state.getBadlyGuessedLetters(), maskedState.badlyGuessedLetters);
    assertEquals(state.getCategory(), maskedState.category);
    assertEquals(state.getFeatures(), maskedState.features);
    assertEquals(state.getGuessedLetters(), maskedState.guessedLetters);
    assertEquals(state.getPenalties(), maskedState.penalties);
    assertEquals(state.getPenaltiesRemaining(), maskedState.penaltiesRemaining);
    assertEquals(state.getMaxPenalties(), maskedState.maxPenalties);
    assertEquals(state.isPuzzleOver(), maskedState.isPuzzleOver);
    assertEquals(state.isPuzzleSolved(), maskedState.isPuzzleSolved);
    assertEquals(state.isPlayerHung(), maskedState.isPlayerHung);
  }

  private void checkPartialMaskedData(MaskedIndividualGameState maskedState,
      IndividualGameState state) {
    assertEquals(state.getBlanksRemaining(), maskedState.blanksRemaining);
    assertEquals(state.getCategory(), maskedState.category);
    assertEquals(state.getFeatures(), maskedState.features);
    assertEquals(state.getPenalties(), maskedState.penalties);
    assertEquals(state.getPenaltiesRemaining(), maskedState.penaltiesRemaining);
    assertEquals(state.getMaxPenalties(), maskedState.maxPenalties);
    assertEquals(state.isPuzzleOver(), maskedState.isPuzzleOver);
    assertEquals(maskedState.isPuzzleSolved, state.isPuzzleSolved());
    assertEquals(state.isPlayerHung(), maskedState.isPlayerHung);
    assertTrue(maskedState.featureData.isEmpty());
    assertEquals("", maskedState.wordPhrase);
    assertTrue(maskedState.badlyGuessedLetters.isEmpty());
    assertTrue(maskedState.guessedLetters.isEmpty());
  }

  private void checkUnmaskedGameFields(MaskedGame maskedGame, Game game) {
    assertEquals(game.getId().toHexString(), maskedGame.getId());
    assertEquals(
        (game.getCompletedTimestamp() != null ? game.getCompletedTimestamp()
            .toEpochMilli() : null), maskedGame.getCompletedTimestamp());
    assertEquals(
        (game.getCreated() != null ? game.getCreated().toEpochMilli()
            : null), maskedGame.getCreated());
    assertEquals(
        (game.getDeclinedTimestamp() != null ? game.getDeclinedTimestamp()
            .toEpochMilli() : null), maskedGame.getDeclinedTimestamp());
    assertEquals(
        (game.getLastUpdate() != null ? game.getLastUpdate().toEpochMilli()
            : null), maskedGame.getLastUpdate());
    assertEquals(game.getFeatures(), maskedGame.getFeatures());
  }

  private void checkMultiPlayerGame(MaskedGame maskedGame) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>(3);
    map.put(PONE.getMd5(), PONE.getDisplayName());
    map.put(PTWO.getMd5(), PTWO.getDisplayName());
    map.put(PTHREE.getMd5(), PTHREE.getDisplayName());
    assertEquals(map, maskedGame.getPlayers());
    LinkedHashMap<String, String> map1 = new LinkedHashMap<>(3);
    map1.put(PONE.getMd5(), PONE.getImageUrl());
    map1.put(PTWO.getMd5(), PTWO.getImageUrl());
    map1.put(PTHREE.getMd5(), PTHREE.getImageUrl());
    assertEquals(map1, maskedGame.getPlayerImages());
    LinkedHashMap<String, String> map2 = new LinkedHashMap<>(3);
    map2.put(PONE.getMd5(), PONE.getProfileUrl());
    map2.put(PTWO.getMd5(), PTWO.getProfileUrl());
    map2.put(PTHREE.getMd5(), PTHREE.getProfileUrl());
    assertEquals(map2, maskedGame.getPlayerProfiles());

    assertEquals(PTWO.getMd5(), maskedGame.getInitiatingPlayer());
    LinkedHashMap<String, PlayerState> map3 = new LinkedHashMap<>(3);
    map3.put(PONE.getMd5(), PlayerState.Accepted);
    map3.put(PTWO.getMd5(), PlayerState.Rejected);
    map3.put(PTHREE.getMd5(), PlayerState.Pending);
    assertEquals(map3, maskedGame.getPlayerStates());
    LinkedHashMap<String, Integer> map4 = new LinkedHashMap<>(3);
    map4.put(PONE.getMd5(), 5);
    map4.put(PTWO.getMd5(), 7);
    map4.put(PTHREE.getMd5(), -10);
    assertEquals(map4, maskedGame.getPlayerRunningScores());
    LinkedHashMap<String, Integer> map5 = new LinkedHashMap<>(3);
    map5.put(PONE.getMd5(), 1);
    map5.put(PTWO.getMd5(), 0);
    map5.put(PTHREE.getMd5(), -1);
    assertEquals(map5, maskedGame.getPlayerRoundScores());
    LinkedHashMap<GameFeature, String> map6 = new LinkedHashMap<>(2);
    map6.put(GameFeature.DrawFace, "");
    map6.put(GameFeature.SingleWinner, PTWO.getMd5());
    assertEquals(map6, maskedGame.getFeatureData());
  }

  private Game makeMultiPlayerGame(MongoPlayer puzzler,
      LinkedHashMap<ObjectId, IndividualGameState> states) {
    Game game = new Game();

    LinkedHashMap<GameFeature, Object> featureDate = new LinkedHashMap<>(
        2);
    featureDate.put(GameFeature.DrawFace, "");
    featureDate.put(GameFeature.SingleWinner, PTWO.getId());

    LinkedHashMap<ObjectId, PlayerState> map1 = new LinkedHashMap<>(3);
    map1.put(PONE.getId(), PlayerState.Accepted);
    map1.put(PTWO.getId(), PlayerState.Rejected);
    map1.put(PTHREE.getId(), PlayerState.Pending);

    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(3);
    map2.put(PONE.getId(), 5);
    map2.put(PTWO.getId(), 7);
    map2.put(PTHREE.getId(), -10);

    LinkedHashMap<ObjectId, Integer> map3 = new LinkedHashMap<>(3);
    map3.put(PONE.getId(), 1);
    map3.put(PTWO.getId(), 0);
    map3.put(PTHREE.getId(), -1);

    game.setGamePhase(GamePhase.Playing);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE));
    game.setWordPhraseSetter(puzzler.getId());
    game.setCreated(Instant.now());
    game.setCompletedTimestamp(Instant.now());
    game.setDeclinedTimestamp(Instant.now());
    game.setFeatureData(featureDate);
    game.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.SystemPuzzles, GameFeature.SinglePlayer)));
    game.setId(new ObjectId());
    game.setInitiatingPlayer(PTWO.getId());
    game.setLastUpdate(Instant.now());
    game.setPlayerStates(map1);
    game.setPlayerRunningScores(map2);
    game.setPlayerRoundScores(map3);
    game.setPreviousId(new ObjectId());
    game.setRematchTimestamp(Instant.now());
    game.setSolverStates(states);
    game.setRound(new Random().nextInt(1000));
    game.setVersion(10);
    return game;
  }
}
