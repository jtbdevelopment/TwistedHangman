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
public class SinglePlayerGameValidator implements GameValidator<Game> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SinglePlayerGameValidator.class);
  private static final String ERROR = "Game's single player marker is wrong.";

  @Override
  public boolean validateGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.SinglePlayer)) {
      if (game.getPlayers().size() != 1) {
        LOGGER.warn("Managed to create single player game without single player. " + game);
        return false;
      }

    } else {
      if (game.getPlayers().size() == 1) {
        LOGGER.warn("Managed to miss marking single player game. " + game);
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
