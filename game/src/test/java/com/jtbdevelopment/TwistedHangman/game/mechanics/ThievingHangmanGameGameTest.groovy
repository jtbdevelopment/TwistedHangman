package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState

/**
 * Date: 10/25/14
 * Time: 8:21 PM
 */
class ThievingHangmanGameGameTest extends AbstractThievingHangmanGameTest<ThievingHangmanGame> {

    @Override
    protected ThievingHangmanGame makeGame(final String wordPhrase, final String category, final int maxPenalties) {
        return new ThievingHangmanGame(makeGameState(wordPhrase, category, maxPenalties))
    }

    @Override
    protected Set<HangmanGameState.GameFeatures> getGameFeatures() {
        return [HangmanGameState.GameFeatures.ThievingCountTracking, HangmanGameState.GameFeatures.ThievingPositionTracking] as Set
    }
}
