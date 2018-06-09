package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 8:40 PM
 */
public class ValidFeatureSetGameValidatorTest extends TwistedHangmanTestCase {

  private ValidFeatureSetGameValidator validator = new ValidFeatureSetGameValidator();

  @Test
  public void testErrorMessage() {
    assertEquals("System Error - Combination of features is not valid somehow.",
        validator.errorMessage());
  }

  @Test
  public void testValidAreValid() {
    GameFeature.getAllowedCombinations().forEach(combo -> {
      Game game = makeSimpleGame();
      game.setFeatures(combo);
      assertTrue(validator.validateGame(game));
    });
  }

  @Test
  public void testValidAreValidWithNonValidatedField() {
    final List<GameFeature> nonValidated = Arrays.stream(GameFeature.values())
        .filter(f -> !f.getValidate()).collect(Collectors.toList());
    GameFeature.getAllowedCombinations().forEach(combo -> {
      Game game = makeSimpleGame();
      game.setFeatures(new HashSet<>(combo));
      game.getFeatures().addAll(nonValidated);
      assertTrue(validator.validateGame(game));
    });
  }

  @Test
  public void testInvalidAreInvalidPartialSet() {
    //  Full set takes too long for unit
    Set<GameFeature> features = Sets.newHashSet(
        GameFeature.SingleWinner, GameFeature.SystemPuzzles, GameFeature.ThreePlus,
        GameFeature.AlternatingPuzzleSetter, GameFeature.TwoPlayer, GameFeature.DrawFace);
    final Game game = makeSimpleGame();

    Sets.powerSet(features).forEach(powerSet -> {
      game.setFeatures(powerSet);
      assertEquals(
          GameFeature.getAllowedCombinations().contains(powerSet),
          validator.validateGame(game));
    });
  }
}
