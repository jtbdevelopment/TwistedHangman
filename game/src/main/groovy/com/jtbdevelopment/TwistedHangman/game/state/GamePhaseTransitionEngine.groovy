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
            case Game.GamePhase.Challenge:
                def reject = game.playerStates.values().find { Game.PlayerChallengeState it -> it == Game.PlayerChallengeState.Rejected }
                if (reject != null) {
                    return changeStateAndReevaluate(Game.GamePhase.Declined, game)
                } else {
                    def pending = game.playerStates.values().find { Game.PlayerChallengeState it -> it == Game.PlayerChallengeState.Pending }
                    if (pending == null) {
                        return changeStateAndReevaluate(Game.GamePhase.Setup, game)
                    }
                }
                break;
            case Game.GamePhase.Setup:
                def pendingWP = game.solverStates.values().find { IndividualGameState gameState -> StringUtils.isEmpty(gameState.wordPhraseString) }
                if (pendingWP == null) {
                    return changeStateAndReevaluate(Game.GamePhase.Playing, game)
                }
                break;
            case Game.GamePhase.Playing:
                def won = game.solverStates.values().find { IndividualGameState it -> it.gameWon }
                def pending = game.solverStates.values().find { IndividualGameState it -> !it.gameOver }
                if (pending == null || (won != null && game.features.contains(GameFeature.SingleWinner))) {
                    return gameScorer.scoreGame(changeStateAndReevaluate(Game.GamePhase.Rematch, game))
                }
                break;
            case Game.GamePhase.Rematch:
                if (game.rematched != null) {
                    return changeStateAndReevaluate(Game.GamePhase.Rematched, game)
                }
                break;
            case Game.GamePhase.Declined:
            case Game.GamePhase.Rematched:
                break;
        }
        return game
    }

    private Game changeStateAndReevaluate(final Game.GamePhase setup, final Game game) {
        game.gamePhase = setup
        evaluateGamePhaseForGame(game)
    }
}
