package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.factory.GameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:14 PM
 */
@Component
public class ThreePlusGameValidator implements GameValidator<Game> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThreePlusGameValidator.class);
  private static final String ERROR = "Game's 3+ player marker is wrong.";

  @Override
  public boolean validateGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.ThreePlus)) {
      if (game.getPlayers().size() < 3) {
        LOGGER.warn("Managed to create 3+ game incorrectly. " + game);
        return false;
      }

    } else {
      if (game.getPlayers().size() > 2) {
        LOGGER.warn("Managed to miss marking 3+ game. " + game);
        return false;
      }

    }

    return true;
  }

  @Override
  public String errorMessage() {
    return ERROR;
  }
}
