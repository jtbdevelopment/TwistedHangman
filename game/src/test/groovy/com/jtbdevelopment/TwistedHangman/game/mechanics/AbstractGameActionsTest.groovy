package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */
abstract class AbstractGameActionsTest extends GroovyTestCase {
    protected Object[] makeWorkingPhraseFromPhrase(final String phrase) {
        char[] working = new char[phrase.length()]
        int blanks = 0;
        for (int i = 0; i < phrase.length(); ++i) {
            if (phrase.charAt(i).isLetter()) {
                working[i] = '_';
                ++blanks;
            } else {
                working[i] = phrase.charAt(i);
            }
        }
        [working, blanks]
    }

    protected IndividualGameState makeGameState(String wordPhrase, String category, int maxPenalties) {
        IndividualGameState gameState = new IndividualGameState(getGameFeatures())
        gameState.wordPhrase = wordPhrase.toUpperCase().toCharArray()
        def phraseAndCount = makeWorkingPhraseFromPhrase(wordPhrase)
        gameState.workingWordPhrase = (char[]) phraseAndCount[0]
        gameState.blanksRemaining = (int) phraseAndCount[1]
        gameState.category = category
        gameState.maxPenalties = maxPenalties
        gameState
    }

    abstract protected Set<GameFeature> getGameFeatures();
}
