package com.jtbdevelopment.TwistedHangman.game.handlers;

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException;
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler.CategoryAndWordPhrase;
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter;
import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import com.jtbdevelopment.games.dictionary.Validator;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Date: 11/9/2014 Time: 9:18 PM
 *
 * param should be map of 2 keys
 */
@Component
public class SetPuzzleHandler extends
    AbstractPlayerRotatingGameActionHandler<CategoryAndWordPhrase> {

  private final Validator validator;
  private final PhraseSetter phraseSetter;

  public SetPuzzleHandler(final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, Game> gameRepository,
      final GameTransitionEngine<Game> transitionEngine,
      final GamePublisher<Game, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, Game, MaskedGame> gameMasker,
      final Validator validator,
      final PhraseSetter phraseSetter
  ) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
    this.validator = validator;
    this.phraseSetter = phraseSetter;
  }

  protected Game handleActionInternal(
      final MongoPlayer player,
      final Game game,
      final CategoryAndWordPhrase param) {
    validatePuzzleStates(game, player);

    final String wordPhrase = param.getWordPhrase();
    final String category = param.getCategory();
    validateWordPhraseAndCategory(wordPhrase, category);

    findPuzzlesToSetForPlayer(game, player).forEach(individualGameState -> phraseSetter
        .setWordPhrase(individualGameState, wordPhrase, category));
    return game;
  }

  private void validateWordPhraseAndCategory(final String wordPhrase, final String category) {
    List<String> invalid = validator
        .validateWordPhrase(wordPhrase, DictionaryType.USEnglishMaximum);
    invalid.addAll(validator.validateWordPhrase(category, DictionaryType.USEnglishMaximum));
    if (invalid.size() > 0) {
      throw new InvalidPuzzleWordsException(invalid);
    }

  }

  private void validatePuzzleStates(final Game game, final MongoPlayer player) {
    if (!game.getGamePhase().equals(GamePhase.Setup)) {
      throw new GameIsNotInSetupPhaseException();
    }

    if (game.getWordPhraseSetter() == null || game.getWordPhraseSetter().equals(player.getId())) {
      if (findPuzzlesToSetForPlayer(game, player).size() == 0) {
        throw new PuzzlesAreAlreadySetException();
      }

    } else {
      throw new PuzzlesAreAlreadySetException();
    }

  }

  private List<IndividualGameState> findPuzzlesToSetForPlayer(
      final Game game,
      final MongoPlayer player) {

    return game.getSolverStates().entrySet().stream().filter(e ->
        StringUtils.isEmpty(e.getValue().getWordPhraseString()) &&
            !player.getId().equals(e.getKey())
    ).map(Entry::getValue).collect(Collectors.toList());
  }

  public static class CategoryAndWordPhrase {

    private String category;
    private String wordPhrase;

    public String getCategory() {
      return category;
    }

    public void setCategory(String category) {
      this.category = category;
    }

    public String getWordPhrase() {
      return wordPhrase;
    }

    public void setWordPhrase(String wordPhrase) {
      this.wordPhrase = wordPhrase;
    }
  }
}
