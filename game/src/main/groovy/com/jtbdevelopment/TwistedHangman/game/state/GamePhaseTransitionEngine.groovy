package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 11/8/2014
 * Time: 4:18 PM
 */
@Component
@CompileStatic
class GamePhaseTransitionEngine {
    @Autowired
    GameRepository gameRepository
    @Autowired
    GameScorer gameScorer

    public Game evaluateGamePhaseForGame(final Game game) {
        switch (game.gamePhase) {
            case GamePhase.Challenge:
                def reject = game.playerStates.values().find { PlayerChallengeState it -> it == PlayerChallengeState.Rejected }
                if (reject != null) {
                    return changeStateAndReevaluate(GamePhase.Declined, game)
                } else {
                    def pending = game.playerStates.values().find { PlayerChallengeState it -> it == PlayerChallengeState.Pending }
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
                def won = game.solverStates.values().find { IndividualGameState it -> it.gameWon }
                def pending = game.solverStates.values().find { IndividualGameState it -> !it.gameOver }
                if (pending == null || (won != null && game.features.contains(GameFeature.SingleWinner))) {
                    return gameScorer.scoreGame(changeStateAndReevaluate(GamePhase.Rematch, game))
                }
                break;
            case GamePhase.Rematch:
                if (game.rematched != null) {
                    return changeStateAndReevaluate(GamePhase.Rematched, game)
                }
                break;
            case GamePhase.Declined:
            case GamePhase.Rematched:
                break;
        }
        return game
    }

    private Game changeStateAndReevaluate(final GamePhase setup, final Game game) {
        game.gamePhase = setup
        evaluateGamePhaseForGame(game)
    }
}
