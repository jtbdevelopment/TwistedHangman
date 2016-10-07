package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.games.dictionary.DictionaryType
import com.jtbdevelopment.games.dictionary.Validator
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 11/9/2014
 * Time: 9:18 PM
 *
 * param should be map of 2 keys
 */
@CompileStatic
@Component
class SetPuzzleHandler extends AbstractPlayerRotatingGameActionHandler<CategoryAndWordPhrase> {
    public static class CategoryAndWordPhrase {
        String category
        String wordPhrase
    }

    @Autowired
    Validator validator
    @Autowired
    PhraseSetter phraseSetter

    @Override
    protected Game handleActionInternal(
            final Player player, final Game game, final CategoryAndWordPhrase param) {
        validatePuzzleStates(game, player)

        String wordPhrase = param.wordPhrase
        String category = param.category
        validateWordPhraseAndCategory(wordPhrase, category)

        findPuzzlesToSetForPlayer(game, player).values().each {
            IndividualGameState gameState ->
                phraseSetter.setWordPhrase(gameState, wordPhrase, category)
        }
        game
    }

    protected void validateWordPhraseAndCategory(final String wordPhrase, final String category) {
        List<String> invalid = validator.validateWordPhrase(wordPhrase, DictionaryType.USEnglishMaximum)
        invalid.addAll(validator.validateWordPhrase(category, DictionaryType.USEnglishMaximum))
        if (invalid.size() > 0) {
            throw new InvalidPuzzleWordsException(invalid)
        }
    }

    protected static void validatePuzzleStates(final Game game, final Player<ObjectId> player) {
        if (game.gamePhase != GamePhase.Setup) {
            throw new GameIsNotInSetupPhaseException();
        }
        if (game.wordPhraseSetter == null || game.wordPhraseSetter == player.id) {
            if (findPuzzlesToSetForPlayer(game, player).size() == 0) {
                throw new PuzzlesAreAlreadySetException();
            }
        } else {
            throw new PuzzlesAreAlreadySetException()
        }
    }

    protected static Map<String, IndividualGameState> findPuzzlesToSetForPlayer(
            final Game game, final Player<ObjectId> player) {
        (Map<String, IndividualGameState>) game.solverStates.findAll {
            ObjectId gamePlayer, IndividualGameState gameState ->
                (player.id != gamePlayer) &&
                        StringUtils.isEmpty(gameState.wordPhraseString)
        }.collectEntries {
            ObjectId gamePlayer, IndividualGameState gameState ->
                return [(gamePlayer.toHexString()): gameState]
        }
    }
}
