package com.jtbdevelopment.TwistedHangman.rest.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.TwistedHangman.rest.services.PlayerServices.FeaturesAndPlayers;
import com.jtbdevelopment.games.rest.handlers.NewGameHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/15/2014 Time: 12:02 PM
 */
public class PlayerServicesTest {

  private NewGameHandler newGameHandler = Mockito.mock(NewGameHandler.class);
  private PlayerServices playerServices = new PlayerServices(null, null, null, null, null,
      newGameHandler);

  @Test
  public void testCreateNewGame() {
    ObjectId APLAYER = new ObjectId();
    playerServices.getPlayerID().set(APLAYER);
    Set<GameFeature> features = new HashSet<>(
        Arrays.asList(GameFeature.AlternatingPuzzleSetter, GameFeature.DrawGallows));
    List<String> players = Arrays.asList("1", "2", "3");
    FeaturesAndPlayers input = new FeaturesAndPlayers();
    input.setFeatures(features);
    input.setPlayers(players);
    THMaskedGame game = new THMaskedGame();
    when(newGameHandler.handleCreateNewGame(APLAYER, players, features)).thenReturn(game);
    assertSame(game, playerServices.createNewGame(input));
  }

  @Test
  public void testCreateNewGameAnnotations() throws NoSuchMethodException {
    Method gameServices = PlayerServices.class.getMethod("createNewGame", FeaturesAndPlayers.class);

    assertEquals(4, gameServices.getAnnotations().length);
    assertTrue(gameServices.isAnnotationPresent(Path.class));
    assertEquals("new", gameServices.getAnnotation(Path.class).value());
    assertTrue(gameServices.isAnnotationPresent(Consumes.class));
    assertArrayEquals(Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
        gameServices.getAnnotation(Consumes.class).value());
    assertTrue(gameServices.isAnnotationPresent(Produces.class));
    assertArrayEquals(
        Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
        gameServices.getAnnotation(Produces.class).value());
    assertTrue(gameServices.isAnnotationPresent(POST.class));
    Annotation[][] params = gameServices.getParameterAnnotations();
    assertEquals(1, params.length);
    assertEquals(0, params[0].length);
  }
}
