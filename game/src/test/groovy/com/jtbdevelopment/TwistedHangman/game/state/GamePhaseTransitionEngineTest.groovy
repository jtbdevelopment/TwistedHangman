package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase

import java.time.ZonedDateTime

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

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == GamePhase.Playing
    }


    public void testChallengeStayingChallenge() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: GamePhase.Challenged,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Pending],
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testChallengeToDeclined() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Challenged,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): PlayerState.Rejected, (PTWO.id): PlayerState.Pending],
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == GamePhase.Declined
    }


    public void testChallengeToSetup() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: GamePhase.Challenged,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Accepted],
                solverStates: [(PONE.id): new IndividualGameState(), (PTWO.id): new IndividualGameState()]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == GamePhase.Setup
    }


    public void testSetupToSetup() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: GamePhase.Setup,
                features: [GameFeature.SystemPuzzles] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                        (PTWO.id): new IndividualGameState(maxPenalties: 5),
                ]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
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

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == GamePhase.Playing
    }


    public void testPlayingToPlaying() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP"),
                        (PTWO.id): new IndividualGameState(wordPhrase: "SETUP"),
                ]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
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
        Game scored = game.clone()
        transitionEngine.gameScorer = [
                scoreGame: {
                    Game it ->
                        assert it.is(game)
                        assert it.gamePhase == GamePhase.RoundOver
                        return scored
                }
        ] as GameScorer


        assert scored.is(transitionEngine.evaluateGamePhaseForGame(game))
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
        Game scored = game.clone()
        transitionEngine.gameScorer = [
                scoreGame: {
                    Game it ->
                        assert it.is(game)
                        assert it.gamePhase == GamePhase.RoundOver
                        return scored
                }
        ] as GameScorer

        assert scored.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testRematchToRematch() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: GamePhase.RoundOver, rematched: null)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testRematchToRematched() {
        assert transitionEngine.gameScorer == null

        Game game = new Game(gamePhase: GamePhase.RoundOver, rematched: ZonedDateTime.now())
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == GamePhase.NextRoundStarted
    }


    public void testRematchedToRematched() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: GamePhase.NextRoundStarted)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testDeclinedToDeclined() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: GamePhase.Declined)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }

    public void testQuitToQuit() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: GamePhase.Quit)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }
}
