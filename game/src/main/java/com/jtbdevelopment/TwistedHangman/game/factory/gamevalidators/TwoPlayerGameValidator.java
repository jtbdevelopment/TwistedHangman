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
public class TwoPlayerGameValidator implements GameValidator<Game> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TwoPlayerGameValidator.class);
  private static final String ERROR = "Game's two player marker is wrong.";

  @Override
  public boolean validateGame(final Game game) {
    if (game.getFeatures().contains(GameFeature.TwoPlayer)) {
      if (game.getPlayers().size() != 2) {
        LOGGER.warn("Managed to create two player game without 2 players. " + game);
        return false;
      }

    } else {
      if (game.getPlayers().size() == 2) {
        LOGGER.warn("Managed to miss marking two player game with 2 players. " + game);
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
