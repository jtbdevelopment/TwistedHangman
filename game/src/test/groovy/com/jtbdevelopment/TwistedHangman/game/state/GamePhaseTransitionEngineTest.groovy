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
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SinglePlayer, GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): Game.PlayerChallengeState.Accepted],
                solverStates: [(PONE.id): new IndividualGameState(maxPenalties: 10, wordPhrase: "SET".toCharArray())]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == Game.GamePhase.Playing
    }


    public void testChallengeStayingChallenge() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): Game.PlayerChallengeState.Accepted, (PTWO.id): Game.PlayerChallengeState.Pending],
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testChallengeToDeclined() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): Game.PlayerChallengeState.Rejected, (PTWO.id): Game.PlayerChallengeState.Pending],
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == Game.GamePhase.Declined
    }


    public void testChallengeToSetup() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): Game.PlayerChallengeState.Accepted, (PTWO.id): Game.PlayerChallengeState.Accepted],
                solverStates: [(PONE.id): new IndividualGameState(), (PTWO.id): new IndividualGameState()]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == Game.GamePhase.Setup
    }


    public void testSetupToSetup() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Setup,
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
                gamePhase: Game.GamePhase.Setup,
                features: [GameFeature.SystemPuzzles] as Set,
                solverStates: [
                        (PONE.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                        (PTWO.id): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                ]
        )

        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == Game.GamePhase.Playing
    }


    public void testPlayingToPlaying() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Playing,
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
                gamePhase: Game.GamePhase.Playing,
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
                        assert it.gamePhase == Game.GamePhase.Rematch
                        return scored
                }
        ] as GameScorer


        assert scored.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testPlayingToRematchSingleWinner() {
        Game game = new Game(
                gamePhase: Game.GamePhase.Playing,
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
                        assert it.gamePhase == Game.GamePhase.Rematch
                        return scored
                }
        ] as GameScorer

        assert scored.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testRematchToRematch() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Rematch, rematched: null)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testRematchToRematched() {
        assert transitionEngine.gameScorer == null

        Game game = new Game(gamePhase: Game.GamePhase.Rematch, rematched: ZonedDateTime.now())
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
        assert game.gamePhase == Game.GamePhase.Rematched
    }


    public void testRematchedToRematched() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Rematched)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }


    public void testDeclinedToDeclined() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Declined)
        assert game.is(transitionEngine.evaluateGamePhaseForGame(game))
    }
}
