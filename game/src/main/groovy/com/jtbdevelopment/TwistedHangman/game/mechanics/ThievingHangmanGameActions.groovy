package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeatures
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 10/25/2014
 * Time: 7:44 PM
 */
@CompileStatic
@Component
class ThievingHangmanGameActions {
    public static final String STEALING_KNOWN_LETTER_ERROR = "Letter already known at position.";
    public static final String CANT_STEAL_ON_FINAL_PENALTY_ERROR = "Can't steal a letter with only one move to losing.";
    public static final String NOT_THIEVING_GAME_ERROR = "This game does not support thieving."
    public static final String POSITION_BEYOND_END_ERROR = "Position is beyond end of word/phrase.";
    public static final String NEGATIVE_POSITION_ERROR = "Can't reveal negative position.";

    /*
    //  TODO - this belongs elsewhere
    public ThievingHangmanGameActions(final HangmanGameState gameState) {
        super(gameState)
        if (!gameState.features.contains(HangmanGameState.GameFeatures.ThievingCountTracking) ||
                !gameState.features.contains(HangmanGameState.GameFeatures.ThievingPositionTracking)) {
            throw new IllegalArgumentException(NOT_THIEVING_GAME_ERROR)
        }
        if (!gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking]) {
            gameState.featureData[HangmanGameState.GameFeatures.ThievingCountTracking] = 0
        }
        if (!gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking]) {
            gameState.featureData[HangmanGameState.GameFeatures.ThievingPositionTracking] =
                    (1..(gameState.wordPhrase.length)).collect() { int c -> false }.toArray()
        }
    }
     */

    void stealLetter(final HangmanGameState gameState, int position) {
        boolean[] markers = gameState.featureData[HangmanGameFeatures.ThievingPositionTracking]
        if (gameState.isGameOver()) {
            throw new IllegalStateException(HangmanGameActions.GAME_OVER_ERROR)
        }
        if (position >= gameState.wordPhrase.length) {
            throw new IllegalArgumentException(POSITION_BEYOND_END_ERROR)
        }
        if (position < 0) {
            throw new IllegalArgumentException(NEGATIVE_POSITION_ERROR)
        }
        if (markers[position] || gameState.wordPhrase[position] == gameState.workingWordPhrase[position]) {
            throw new IllegalArgumentException(STEALING_KNOWN_LETTER_ERROR)
        }
        if (gameState.penaltiesRemaining == 1) {
            throw new IllegalStateException(CANT_STEAL_ON_FINAL_PENALTY_ERROR)
        }

        Integer counter = (Integer) gameState.featureData[HangmanGameFeatures.ThievingCountTracking]
        counter += 1
        markers[position] = true
        gameState.featureData[HangmanGameFeatures.ThievingCountTracking] = counter
        gameState.featureData[HangmanGameFeatures.ThievingPositionTracking] = markers
        gameState.moveCount += 1
        gameState.penalties += 1
        gameState.workingWordPhrase[position] = gameState.wordPhrase[position]
    }
}
