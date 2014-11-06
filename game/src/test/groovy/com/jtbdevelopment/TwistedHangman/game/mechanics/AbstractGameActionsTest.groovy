package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */
abstract class AbstractGameActionsTest extends GroovyTestCase {
    protected char[] makeWorkingPhraseFromPhrase(final String phrase) {
        char[] working = new char[phrase.length()]
        for (int i = 0; i < phrase.length(); ++i) {
            if (phrase.charAt(i).isLetter()) {
                working[i] = '_';
            } else {
                working[i] = phrase.charAt(i);
            }
        }
        working
    }

    protected IndividualGameState makeGameState(String wordPhrase, String category, int maxPenalties) {
        IndividualGameState gameState = new IndividualGameState(getGameFeatures())
        gameState.wordPhrase = wordPhrase.toUpperCase().toCharArray()
        gameState.workingWordPhrase = makeWorkingPhraseFromPhrase(wordPhrase)
        gameState.category = category
        gameState.maxPenalties = maxPenalties
        gameState
    }

    abstract protected Set<GameFeature> getGameFeatures();
}
