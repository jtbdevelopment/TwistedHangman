package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */

//  TODO - test exceptions in constructor and guesser

class HangmanGameTest extends AbstractHangmanGameTest<HangmanGame> {
    @Override
    protected HangmanGame makeGame(final String wordPhrase, final String category, final int maxPenalties) {
        return new HangmanGame(
                makeGameState(wordPhrase, category, maxPenalties))
    }

    @Override
    protected Set<HangmanGameState.GameFeatures> getGameFeatures() {
        return [] as Set
    }
}
