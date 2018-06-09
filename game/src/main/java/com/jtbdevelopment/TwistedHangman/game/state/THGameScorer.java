package com.jtbdevelopment.TwistedHangman.game.state;

import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.state.scoring.GameScorer;
import org.springframework.stereotype.Component;

/**
 * Date: 11/8/2014 Time: 4:59 PM
 */
@Component
public class THGameScorer implements GameScorer<THGame> {

  @Override
  public THGame scoreGame(final THGame game) {
    int[] winners = {0};
    int[] losers = {0};
    game.getSolverStates().forEach((playerId, gameState) -> {
      if (gameState.isPuzzleSolved()) {
        winners[0]++;
        game.getPlayerRoundScores().put(playerId, 1);
        game.getPlayerRunningScores()
            .put(playerId, game.getPlayerRunningScores().get(playerId) + 1);

        //  Move to list of post-scorers?
        if (game.getFeatures().contains(GameFeature.SingleWinner)) {
          game.getFeatureData().put(GameFeature.SingleWinner, playerId);
        }
      }
      if (gameState.isPlayerHung()) {
        losers[0]++;
        game.getPlayerRoundScores().put(playerId, -1);
        game.getPlayerRunningScores()
            .put(playerId, game.getPlayerRunningScores().get(playerId) - 1);
      }
    });

    if (game.getWordPhraseSetter() != null &&
        !game.getWordPhraseSetter().equals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId())) {
      int net = losers[0] - winners[0];
      game.getPlayerRoundScores().put(game.getWordPhraseSetter(), net);
      game.getPlayerRunningScores().put(game.getWordPhraseSetter(),
          game.getPlayerRunningScores().get(game.getWordPhraseSetter()) + net);
    }

    return game;
  }

}
