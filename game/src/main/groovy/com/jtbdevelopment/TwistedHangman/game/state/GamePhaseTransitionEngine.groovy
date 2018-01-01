package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.transition.AbstractMPGamePhaseTransitionEngine
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/8/2014
 * Time: 4:18 PM
 */
@Component
class GamePhaseTransitionEngine extends AbstractMPGamePhaseTransitionEngine<Game> {
    private static final ZoneId GMT = ZoneId.of('GMT')

    @Override
    protected Game evaluateSetupPhase(final Game game) {
        def pendingWP = game.solverStates.values().find { IndividualGameState gameState -> StringUtils.isEmpty(gameState.wordPhraseString) }
        if (pendingWP == null) {
            return changeStateAndReevaluate(GamePhase.Playing, game)
        }
        return game
    }

    @Override
    protected Game evaluatePlayingPhase(final Game game) {
        def won = game.solverStates.values().find { IndividualGameState it -> it.puzzleSolved }
        def pending = game.solverStates.values().find { IndividualGameState it -> !it.puzzleOver }
        if (pending == null || (won != null && game.features.contains(GameFeature.SingleWinner))) {
            game.completedTimestamp = ZonedDateTime.now(GMT)
            return (Game) changeStateAndReevaluate(GamePhase.RoundOver, game)
        }
        return game
    }

}
