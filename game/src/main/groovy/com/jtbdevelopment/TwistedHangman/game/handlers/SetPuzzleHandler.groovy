package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dictionary.Validator
import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotInSetupPhaseException
import com.jtbdevelopment.TwistedHangman.exceptions.input.InvalidPuzzleWordsException
import com.jtbdevelopment.TwistedHangman.exceptions.input.PuzzlesAreAlreadySetException
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
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
class SetPuzzleHandler extends AbstractGameActionHandler<CategoryAndWordPhrase> {
    public static class CategoryAndWordPhrase {
        String category
        String wordPhrase
    }
    public static final String CATEGORY_KEY = "CATEGORY"
    public static final String WORDPHRASE_KEY = "WORDPHRASE"

    @Autowired
    Validator validator
    @Autowired
    PhraseSetter phraseSetter

    @Override
    protected Game handleActionInternal(final Player player, final Game game, final CategoryAndWordPhrase param) {
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

    protected void validateWordPhraseAndCategory(String wordPhrase, String category) {
        List<String> invalid = validator.validateWordPhrase(wordPhrase)
        invalid.addAll(validator.validateWordPhrase(category))
        if (invalid.size() > 0) {
            throw new InvalidPuzzleWordsException(invalid)
        }
    }

    protected static void validatePuzzleStates(Game game, Player player) {
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

    protected static Map<String, IndividualGameState> findPuzzlesToSetForPlayer(final Game game, final Player player) {
        game.solverStates.findAll {
            String gamePlayer, IndividualGameState gameState ->
                (player.id != gamePlayer) &&
                        StringUtils.isEmpty(gameState.wordPhraseString)
        }
    }
}