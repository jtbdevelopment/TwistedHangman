package com.jtbdevelopment.TwistedHangman.rest.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Test;

/**
 * Date: 11/15/2014 Time: 11:28 AM
 */
public class PlayerGatewayServiceTest {

  private PlayerGatewayService playerGatewayService = new PlayerGatewayService(null);

  @Test
  public void testGetFeatures() {
    Map<GameFeature, String> map = new HashMap<>();
    map.put(GameFeature.DrawFace, GameFeature.DrawFace.getDescription());
    map.put(GameFeature.DrawGallows, GameFeature.DrawGallows.getDescription());
    map.put(GameFeature.TurnBased, GameFeature.TurnBased.getDescription());
    map.put(GameFeature.Live, GameFeature.Live.getDescription());
    map.put(GameFeature.SingleWinner, GameFeature.SingleWinner.getDescription());
    map.put(GameFeature.AllComplete, GameFeature.AllComplete.getDescription());
    map.put(GameFeature.AlternatingPuzzleSetter,
        GameFeature.AlternatingPuzzleSetter.getDescription());
    map.put(GameFeature.SystemPuzzles, GameFeature.SystemPuzzles.getDescription());
    map.put(GameFeature.Head2Head, GameFeature.Head2Head.getDescription());
    map.put(GameFeature.Thieving, GameFeature.Thieving.getDescription());
    assertEquals(map, playerGatewayService.featuresAndDescriptions());
  }

  @Test
  public void testGetFeaturesAnnotations() throws NoSuchMethodException {
    Method gameServices = PlayerGatewayService.class
        .getMethod("featuresAndDescriptions");
    assertEquals(3, DefaultGroovyMethods.size(gameServices.getAnnotations()));
    assertTrue(gameServices.isAnnotationPresent(Path.class));
    assertEquals("features", gameServices.getAnnotation(Path.class).value());
    assertTrue(gameServices.isAnnotationPresent(GET.class));
    assertTrue(gameServices.isAnnotationPresent(Produces.class));
    assertArrayEquals(
        Arrays.asList(MediaType.APPLICATION_JSON).toArray(),
        gameServices.getAnnotation(Produces.class).value());
    Annotation[][] params = gameServices.getParameterAnnotations();
    assertEquals(0, params.length);
  }
}
