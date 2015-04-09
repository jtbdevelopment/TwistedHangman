package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState

/**
 * Date: 11/9/14
 * Time: 9:33 AM
 */
class GamePhaseTransitionEngineTest extends TwistedHangmanTestCase {
    GamePhaseTransitionEngine transitionEngine = new GamePhaseTransitionEngine()


    public void testSinglePlayerChallengeTransitionsToPlaying() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Challenged,
                features: [GameFeature.SinglePlayer, GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): PlayerState.Accepted],
                solverStates: [(PONE.id): new IndividualGameState(maxPenalties: 10, wordPhrase: "SET".toCharArray())]
        )

        assert game.is(transitionEngine.evaluateGame(game))
        assert game.gamePhase == GamePhase.Playing
    }


    public void testSetupToSetup() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Setup,
                features: [GameFeature.SystemPuzzles] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                        (PTWO.id): new IndividualGameState(maxPenalties: 5),
                ]
        )

        assert game.is(transitionEngine.evaluateGame(game))
    }


    public void testSetupToPlaying() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Setup,
                features: [GameFeature.SystemPuzzles] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                        (PTWO.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                ]
        )

        assert game.is(transitionEngine.evaluateGame(game))
        assert game.gamePhase == GamePhase.Playing
    }


    public void testPlayingToPlaying() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP"),
                        (PTWO.id): new IndividualGameState(wordPhrase: "SETUP"),
                ]
        )

        assert game.is(transitionEngine.evaluateGame(game))
    }


    public void testPlayingToRematchMultipleWinners() {
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTWO.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTHREE.id): new IndividualGameState(wordPhrase: "SETUP", penalties: IndividualGameState.BASE_PENALTIES)
                ]
        )
        Game scored = (Game) game.clone()
        transitionEngine.gameScorer = [
                scoreGame: {
                    Game it ->
                        assert it.is(game)
                        assert it.gamePhase == GamePhase.RoundOver
                        assert it.completedTimestamp
                        return scored
                }
        ] as GameScorerImpl


        assert scored.is(transitionEngine.evaluateGame(game))
    }


    public void testPlayingToRematchSingleWinner() {
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                features: [GameFeature.SingleWinner] as Set,
                solverStates: [
                        (PONE.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTWO.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SET__"),
                        (PTHREE.id): new IndividualGameState(wordPhrase: "SETUP", penalties: IndividualGameState.BASE_PENALTIES)
                ]
        )
        Game scored = (Game) game.clone()
        transitionEngine.gameScorer = [
                scoreGame: {
                    Game it ->
                        assert it.is(game)
                        assert it.gamePhase == GamePhase.RoundOver
                        assert it.completedTimestamp
                        return scored
                }
        ] as GameScorerImpl

        assert scored.is(transitionEngine.evaluateGame(game))
    }
}
