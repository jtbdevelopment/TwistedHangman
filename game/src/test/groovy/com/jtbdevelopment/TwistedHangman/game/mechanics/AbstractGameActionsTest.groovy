package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */

//  TODO - test exceptions in constructor and guesser

abstract class AbstractGameActionsTest extends GroovyTestCase {
    private HangmanGameActions hangmanGameActions = new HangmanGameActions()

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

    protected HangmanGameState makeGameState(String wordPhrase, String category, int maxPenalties) {
        new HangmanGameState(
                wordPhrase.toUpperCase().toCharArray(),
                makeWorkingPhraseFromPhrase(wordPhrase),
                category,
                maxPenalties,
                getGameFeatures())
    }

    abstract protected Set<HangmanGameState.GameFeatures> getGameFeatures();

}
