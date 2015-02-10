package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.games.games.PlayerState
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/8/2014
 * Time: 4:18 PM
 */
@Component
@CompileStatic
class GamePhaseTransitionEngine {
    private static final ZoneId GMT = ZoneId.of('GMT')

    @Autowired
    GameScorer gameScorer

    public Game evaluateGamePhaseForGame(final Game game) {
        switch (game.gamePhase) {
            case GamePhase.Challenged:
                def reject = game.playerStates.values().find { PlayerState it -> it == PlayerState.Rejected }
                if (reject != null) {
                    return changeStateAndReevaluate(GamePhase.Declined, game)
                } else {
                    def pending = game.playerStates.values().find { PlayerState it -> it == PlayerState.Pending }
                    if (pending == null) {
                        return changeStateAndReevaluate(GamePhase.Setup, game)
                    }
                }
                break;
            case GamePhase.Setup:
                def pendingWP = game.solverStates.values().find { IndividualGameState gameState -> StringUtils.isEmpty(gameState.wordPhraseString) }
                if (pendingWP == null) {
                    return changeStateAndReevaluate(GamePhase.Playing, game)
                }
                break;
            case GamePhase.Playing:
                def won = game.solverStates.values().find { IndividualGameState it -> it.puzzleSolved }
                def pending = game.solverStates.values().find { IndividualGameState it -> !it.puzzleOver }
                if (pending == null || (won != null && game.features.contains(GameFeature.SingleWinner))) {
                    game.completedTimestamp = ZonedDateTime.now(GMT)
                    return gameScorer.scoreGame(changeStateAndReevaluate(GamePhase.RoundOver, game))
                }
                break;
            case GamePhase.RoundOver:
                if (game.rematchTimestamp != null) {
                    return changeStateAndReevaluate(GamePhase.NextRoundStarted, game)
                }
                break;
            case GamePhase.Declined:
            case GamePhase.NextRoundStarted:
            case GamePhase.Quit:
                break;
        }
        return game
    }

    private Game changeStateAndReevaluate(final GamePhase setup, final Game game) {
        game.gamePhase = setup
        evaluateGamePhaseForGame(game)
    }
}
