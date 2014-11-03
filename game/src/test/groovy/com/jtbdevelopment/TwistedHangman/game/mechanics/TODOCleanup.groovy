package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import org.junit.Test

/**
 * Date: 10/26/2014
 * Time: 8:55 PM
 *
 */
//  TODO - one test belongs elsewhere
abstract class TODOCleanup<T extends HangmanGameActions> extends GroovyTestCase {


    @Test
    public void testInitialGameStateForPhraseWithNonAlphabeticCharacters() {
        HangmanGameState gameState = makeGame("It's a wonderful life.", "Movie", 7).gameState
        assert gameState.workingWordPhraseString == "__'_ _ _________ ____."
        assert gameState.wordPhraseString == "IT'S A WONDERFUL LIFE."
        assert gameState.maxPenalties == 7
        assert gameState.penaltiesRemaining == 7
        assert gameState.category == "Movie"
    }


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

    //abstract protected T makeGame(final String wordPhrase, final String category, final int maxPenalties)

    abstract protected Set<HangmanGameState.GameFeatures> getGameFeatures();
}
