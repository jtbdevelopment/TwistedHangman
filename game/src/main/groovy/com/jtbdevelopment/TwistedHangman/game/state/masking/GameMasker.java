package com.jtbdevelopment.TwistedHangman.game.state.masking;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.mongo.state.masking.AbstractMongoMultiPlayerGameMasker;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/14/14 Time: 6:17 PM
 */
@Component
public class GameMasker extends AbstractMongoMultiPlayerGameMasker<GameFeature, Game, MaskedGame> {

  @Override
  protected void copyMaskedData(final Game game, final Player<ObjectId> player,
      final MaskedGame playerMaskedGame, final Map<ObjectId, Player<ObjectId>> idMap) {
    super.copyMaskedData(game, player, playerMaskedGame, idMap);
    game.getPlayerRunningScores().forEach((id, score) -> playerMaskedGame.getPlayerRunningScores()
        .put(idMap.get(id).getMd5(), score));
    game.getPlayerRoundScores().forEach(
        (id, score) -> playerMaskedGame.getPlayerRoundScores().put(idMap.get(id).getMd5(), score));
    game.getSolverStates()
        .forEach((id, state) -> playerMaskedGame.getSolverStates().put(
            idMap.get(id).getMd5(),
            maskGameState(player, game, idMap.get(id), state, idMap)));
    playerMaskedGame.setWordPhraseSetter(
        game.getWordPhraseSetter() != null ?
            game.getWordPhraseSetter().equals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId())
                ? TwistedHangmanSystemPlayerCreator.TH_PLAYER.getMd5()
                : idMap.get(game.getWordPhraseSetter()).getMd5()
            : null);
  }

  private MaskedIndividualGameState maskGameState(
      final Player<ObjectId> playerMaskingFor,
      final Game game,
      final Player<ObjectId> gameStatePlayer,
      final IndividualGameState gameState,
      final Map<ObjectId, Player<ObjectId>> idMap) {
    final MaskedIndividualGameState masked = new MaskedIndividualGameState();

    if (game.getGamePhase().equals(GamePhase.RoundOver) ||
        game.getGamePhase().equals(GamePhase.NextRoundStarted) ||
        game.getGamePhase().equals(GamePhase.Quit) ||
        playerMaskingFor.equals(gameStatePlayer) ||
        game.getWordPhraseSetter().equals(playerMaskingFor.getId()) ||
        (game.getWordPhraseSetter() == null && !playerMaskingFor.equals(gameStatePlayer))) {
      masked.workingWordPhrase = gameState.getWorkingWordPhraseString();
      masked.guessedLetters.addAll(gameState.getGuessedLetters());
      masked.badlyGuessedLetters.addAll(gameState.getBadlyGuessedLetters());
      gameState.getFeatureData().forEach((feature, data) -> {
        if (data instanceof ObjectId && idMap.containsKey(data)) {
          masked.featureData.put(feature, idMap.get(data).getMd5());
        } else {
          masked.featureData.put(feature, data);
        }
      });
    } else {
      masked.workingWordPhrase = "";
    }

    if (game.getGamePhase().equals(GamePhase.RoundOver) ||
        game.getGamePhase().equals(GamePhase.NextRoundStarted) ||
        game.getGamePhase().equals(GamePhase.Quit) ||
        game.getWordPhraseSetter().equals(playerMaskingFor.getId()) ||
        (game.getWordPhraseSetter() == null && !playerMaskingFor.equals(gameStatePlayer))) {
      masked.wordPhrase = gameState.getWordPhraseString();
    } else {
      masked.wordPhrase = "";
    }

    masked.category = gameState.getCategory();
    masked.features.addAll(gameState.getFeatures());
    masked.isPlayerHung = gameState.isPlayerHung();
    masked.isPuzzleSolved = gameState.isPuzzleSolved();
    masked.isPuzzleOver = gameState.isPuzzleOver();
    masked.moveCount = gameState.getMoveCount();
    masked.maxPenalties = gameState.getMaxPenalties();
    masked.penalties = gameState.getPenalties();
    masked.penaltiesRemaining = gameState.getPenaltiesRemaining();
    masked.blanksRemaining = gameState.getBlanksRemaining();
    return masked;
  }

  @Override
  protected MaskedGame newMaskedGame() {
    return new MaskedGame();
  }
}
