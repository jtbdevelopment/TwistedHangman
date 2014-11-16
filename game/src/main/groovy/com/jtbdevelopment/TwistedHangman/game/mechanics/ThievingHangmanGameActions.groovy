package com.jtbdevelopment.TwistedHangman.game.mechanics

import com.jtbdevelopment.TwistedHangman.exceptions.input.*
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 10/25/2014
 * Time: 7:44 PM
 */
@CompileStatic
@Component
class ThievingHangmanGameActions {

    void stealLetter(final IndividualGameState gameState, int position) {
        boolean[] markers = (boolean[]) gameState.featureData[GameFeature.ThievingPositionTracking]
        validateSteal(gameState, position, markers)

        Integer counter = (Integer) gameState.featureData[GameFeature.ThievingCountTracking]
        counter += 1
        markers[position] = true
        gameState.featureData[GameFeature.ThievingCountTracking] = counter
        gameState.featureData[GameFeature.ThievingPositionTracking] = markers
        gameState.moveCount += 1
        gameState.penalties += 1
        gameState.workingWordPhrase[position] = gameState.wordPhrase[position]
        gameState.workingWordPhraseString = new String(gameState.workingWordPhrase)
    }

    protected void validateSteal(IndividualGameState gameState, int position, boolean[] markers) {
        if (gameState.isGameOver()) {
            throw new GameOverException()
        }
        if (position >= gameState.wordPhrase.length) {
            throw new StealingPositionBeyondEndException()
        }
        if (position < 0) {
            throw new StealingNegativePositionException()
        }
        if (markers[position] || gameState.wordPhrase[position] == gameState.workingWordPhrase[position]) {
            throw new StealingKnownLetterException()
        }
        if (gameState.penaltiesRemaining == 1) {
            throw new StealingOnFinalPenaltyException()
        }
    }
}
