package com.jtbdevelopment.TwistedHangman.game.handlers;

import static org.mockito.Mockito.when;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler.CategoryAndWordPhrase;
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import com.jtbdevelopment.games.dictionary.Validator;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/10/14 Time: 7:04 AM
 */
public class SetPuzzleHandlerTest extends TwistedHangmanTestCase {

  private Validator validator = Mockito.mock(Validator.class);
  private PhraseSetter phraseSetter = Mockito.mock(PhraseSetter.class);
  private SetPuzzleHandler handler = new SetPuzzleHandler(null, null, null, null, null, null,
      validator, phraseSetter);

  @Test
  public void testMultiPlayerSettingPuzzle() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(PTHREE.getId());
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    Assert.assertSame(game,
        handler.handleActionInternal(PTHREE, game, phrase));
    Mockito.verify(phraseSetter).setWordPhrase(poneState, wp, c);
    Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c);
  }

  @Test
  public void testTwoPlayerSettingPuzzle() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(null);
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    Assert.assertSame(game,
        handler.handleActionInternal(PONE, game, phrase));
    Mockito.verify(phraseSetter, Mockito.never()).setWordPhrase(poneState, wp, c);
    Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c);
  }

  @Test(expected = PuzzlesAreAlreadySetException.class)
  public void testMultiPlayerSettingPuzzleSecondTimeExceptions() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(PTHREE.getId());
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    Assert.assertSame(game,
        handler.handleActionInternal(PTHREE, game, phrase));
    Mockito.verify(phraseSetter).setWordPhrase(poneState, wp, c);
    Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c);

    poneState.setWordPhrase("SET".toCharArray());
    ptwoState.setWordPhrase("SET".toCharArray());
    CategoryAndWordPhrase phrase1 = new CategoryAndWordPhrase();
    phrase1.setCategory(c);
    phrase1.setWordPhrase(wp);
    handler.handleActionInternal(PTHREE, game, phrase);
  }

  @Test(expected = PuzzlesAreAlreadySetException.class)
  public void testTwoPlayerSettingPuzzleSecondTimeExceptions() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(null);
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    Assert.assertSame(game,
        handler.handleActionInternal(PONE, game, phrase));
    Mockito.verify(phraseSetter, Mockito.never()).setWordPhrase(poneState, wp, c);
    Mockito.verify(phraseSetter).setWordPhrase(ptwoState, wp, c);

    ptwoState.setWordPhrase("SET".toCharArray());
    CategoryAndWordPhrase phrase1 = new CategoryAndWordPhrase();
    phrase1.setCategory(c);
    phrase1.setWordPhrase(wp);
    handler.handleActionInternal(PONE, game, phrase);
  }

  @Test(expected = InvalidPuzzleWordsException.class)
  public void testInvalidWordPhraseCategory() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(null);
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(Arrays.asList("1", "2"));
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(Collections.emptyList());

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    handler.handleActionInternal(PONE, game, phrase);
  }

  @Test(expected = GameIsNotInSetupPhaseException.class)
  public void testPuzzleNotInSetupPhase() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Playing);
    game.setWordPhraseSetter(null);
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    handler.handleActionInternal(PONE, game, phrase);
  }

  @Test(expected = PuzzlesAreAlreadySetException.class)
  public void testMultiPlayerInvalidPlayerSettingPuzzle() {
    String wp = "A PUZZLE";
    String c = "CATEGORY";
    THGame game = new THGame();

    game.setGamePhase(GamePhase.Setup);
    game.setWordPhraseSetter(PTHREE.getId());
    IndividualGameState poneState = new IndividualGameState();
    IndividualGameState ptwoState = new IndividualGameState();
    LinkedHashMap<ObjectId, IndividualGameState> map = new LinkedHashMap<>(
        2);
    map.put(PONE.getId(), poneState);
    map.put(PTWO.getId(), ptwoState);
    game.setSolverStates(map);
    when(validator.validateWordPhrase(wp, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());
    when(validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum))
        .thenReturn(new ArrayList<>());

    CategoryAndWordPhrase phrase = new CategoryAndWordPhrase();
    phrase.setCategory(c);
    phrase.setWordPhrase(wp);
    handler.handleActionInternal(PONE, game, phrase);
  }
}
