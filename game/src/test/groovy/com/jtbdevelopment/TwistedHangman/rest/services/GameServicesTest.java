package com.jtbdevelopment.TwistedHangman.rest.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.game.handlers.GuessLetterHandler;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler.CategoryAndWordPhrase;
import com.jtbdevelopment.TwistedHangman.game.handlers.StealLetterHandler;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/15/2014 Time: 1:49 PM
 */
public class GameServicesTest {

  private ObjectId PID = new ObjectId();
  private ObjectId GID = new ObjectId();
  private MaskedGame result = new MaskedGame();
  private SetPuzzleHandler puzzleHandler = Mockito.mock(SetPuzzleHandler.class);
  private StealLetterHandler stealLetterHandler = Mockito.mock(StealLetterHandler.class);
  private GuessLetterHandler guessLetterHandler = Mockito.mock(GuessLetterHandler.class);
  private GameServices services = new GameServices(null, null, null, null, null, stealLetterHandler,
      guessLetterHandler, puzzleHandler);

  @Before
  public void setUp() {
    services.getPlayerID().set(PID);
    services.getGameID().set(GID);
  }

  @Test
  public void testActionAnnotations() {

    Map<String, List<Object>> map = new HashMap<>();
    map.put(
        "setPuzzle",
        Arrays.asList(
            "puzzle",
            Collections.singletonList(CategoryAndWordPhrase.class),
            new ArrayList<>(),
            Collections.singletonList(MediaType.APPLICATION_JSON)));
    map.put(
        "stealLetter",
        Arrays.asList(
            "steal/{position}",
            Collections.singletonList(int.class),
            Collections.singletonList("position"),
            new ArrayList<>()));
    map.put(
        "guessLetter",
        Arrays.asList(
            "guess/{letter}",
            Collections.singletonList(String.class),
            Collections.singletonList("letter"),
            new ArrayList<>()));
    map.forEach((method, details) -> {
      Method m;
      try {
        Class[] parameterTypes = ((List<Class>) details.get(1)).toArray(new Class[0]);
        m = GameServices.class.getMethod(method, parameterTypes);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
      int expectedA = 3 + ((List) details.get(3)).size();
      assertEquals(expectedA, m.getAnnotations().length);
      assert m.isAnnotationPresent(PUT.class);
      assert m.isAnnotationPresent(Produces.class);
      assertArrayEquals(
          Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
          m.getAnnotation(Produces.class).value());
      assert m.isAnnotationPresent(Path.class);
      assert m.getAnnotation(Path.class).value().equals(details.get(0));
      if (((List) details.get(3)).size() > 0) {
        assert m.isAnnotationPresent(Consumes.class);
        assertArrayEquals(((List) details.get(3)).toArray(),
            m.getAnnotation(Consumes.class).value());
      }

      List<String> pathParams = Arrays.stream(m.getParameterAnnotations())
          .filter(a -> a.length == 1)
          .filter(a -> a[0] instanceof PathParam)
          .map(a -> ((PathParam) a[0]).value()).collect(Collectors
              .toList());
      assertEquals(details.get(2), pathParams);

    });
  }

  @Test
  public void testSetPuzzle() {
    String c = "Cat";
    String w = "Anim";
    CategoryAndWordPhrase wordPhrase = new CategoryAndWordPhrase();

    wordPhrase.setCategory(c);
    wordPhrase.setWordPhrase(w);
    Mockito.when(puzzleHandler.handleAction(PID, GID, wordPhrase)).thenReturn(result);
    assert DefaultGroovyMethods.is(result, services.setPuzzle(wordPhrase));
  }

  @Test
  public void testStealLetter() {
    int pos = 4;
    Mockito.when(stealLetterHandler.handleAction(PID, GID, pos)).thenReturn(result);
    assert DefaultGroovyMethods.is(result, services.stealLetter(pos));
  }

  @Test
  public void testGuessLetter() {
    String guess = "G";
    Mockito.when(guessLetterHandler.handleAction(PID, GID, "G".charAt(0))).thenReturn(result);
    assert DefaultGroovyMethods.is(result, services.guessLetter(guess));
  }

  @Test
  public void testGuessLetterNull() {
    Mockito.when(guessLetterHandler.handleAction(PID, GID, " ".charAt(0))).thenReturn(result);
    assert DefaultGroovyMethods.is(result, services.guessLetter(null));
  }

  @Test
  public void testGuessLetterEmpty() {
    String guess = "";
    Mockito.when(guessLetterHandler.handleAction(PID, GID, " ".charAt(0))).thenReturn(result);
    assert DefaultGroovyMethods.is(result, services.guessLetter(guess));
  }
}
