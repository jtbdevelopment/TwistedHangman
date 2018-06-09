package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.factory.GameValidator;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/14 Time: 6:36 PM
 */
@Component
public class ValidFeatureSetGameValidator implements GameValidator<THGame> {

  private static final String ERROR = "System Error - Combination of features is not valid somehow.";
  private static final Logger LOGGER = LoggerFactory.getLogger(ValidFeatureSetGameValidator.class);

  @Override
  public String errorMessage() {
    return ERROR;
  }

  @Override
  public boolean validateGame(final THGame game) {
    Set<GameFeature> validatingFeatures = game.getFeatures().stream()
        .filter(GameFeature::getValidate)
        .collect(Collectors.toSet());
    if (!GameFeature.getAllowedCombinations().contains(validatingFeatures)) {
      LOGGER.warn(
          "Managed to create game with invalid features - " + validatingFeatures + "\n" + game);
      return false;
    }

    return true;
  }
}
