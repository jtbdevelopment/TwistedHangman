package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers;

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import com.jtbdevelopment.TwistedHangman.game.utility.RandomCannedGameFinder;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 11/4/2014 Time: 9:34 PM
 */
@Component
public class SystemPuzzleSetterInitializer implements GameInitializer<THGame> {

  private static final Logger logger = LoggerFactory.getLogger(SystemPuzzleSetterInitializer.class);
  private final RandomCannedGameFinder randomCannedGameFinder;
  private final PhraseSetter phraseSetter;

  SystemPuzzleSetterInitializer(
      final RandomCannedGameFinder randomCannedGameFinder,
      final PhraseSetter phraseSetter) {
    this.randomCannedGameFinder = randomCannedGameFinder;
    this.phraseSetter = phraseSetter;
  }

  public void initializeGame(final THGame game) {
    if (game.getFeatures().contains(GameFeature.SystemPuzzles)) {
      final PreMadePuzzle cannedGame = randomCannedGameFinder.getRandomGame();
      logger.info("System Challenger setting for " + game.getId() + " using canned id " + cannedGame
          .getId());
      game.getSolverStates().values().forEach(state -> phraseSetter
          .setWordPhrase(state, cannedGame.getWordPhrase(), cannedGame.getCategory()));
    }

  }

  public int getOrder() {
    return LATE_ORDER + 100;
  }
}
