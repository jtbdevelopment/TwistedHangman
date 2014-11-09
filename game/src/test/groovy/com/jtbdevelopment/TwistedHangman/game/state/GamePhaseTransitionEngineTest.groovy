package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import org.junit.Test

/**
 * Date: 11/9/14
 * Time: 9:33 AM
 */
class GamePhaseTransitionEngineTest extends THGroovyTestCase {
    GamePhaseTransitionEngine transitionEngine = new GamePhaseTransitionEngine()


    @Test
    public void testSinglePlayerChallengeTransitionsToPlaying() {
        assert transitionEngine.gameScorer == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SinglePlayer, GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE): Game.PlayerChallengeState.Accepted],
                solverStates: [(PONE): new IndividualGameState(maxPenalties: 10, wordPhrase: "SET".toCharArray())]
        )
        Game setup = game.clone()
        setup.gamePhase = Game.GamePhase.Setup
        Game playing = game.clone()
        playing.gamePhase = Game.GamePhase.Playing

        transitionEngine.gameRepository = [
                save: {
                    Game it ->
                        if (it.is(game)) {
                            assert it.gamePhase == Game.GamePhase.Setup
                            return setup
                        } else if (it.is(setup)) {
                            assert it.gamePhase == Game.GamePhase.Playing
                            return playing
                        }
                        fail("unexpected call to save")
                }
        ] as GameRepository

        assert playing == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testChallengeStayingChallenge() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Challenge,
                features: [GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE): Game.PlayerChallengeState.Accepted, (PTWO): Game.PlayerChallengeState.Pending],
        )

        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testChallengeToDeclined() {

    }

    @Test
    public void testChallengeToSetup() {

    }

    @Test
    public void testSetupToSetup() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Setup,
                features: [GameFeature.SystemPuzzles] as Set,
                solverStates: [
                        (PONE): new IndividualGameState(wordPhrase: "SETUP", maxPenalties: 5),
                        (PTWO): new IndividualGameState(maxPenalties: 5),
                ]
        )

        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testSetupToPlaying() {

    }

    @Test
    public void testPlayingToPlaying() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE): new IndividualGameState(wordPhrase: "SETUP"),
                        (PTWO): new IndividualGameState(wordPhrase: "SETUP"),
                ]
        )

        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testPlayingToRematchMultipleWinners() {
        assert transitionEngine.gameScorer == null
        assert transitionEngine.gameRepository == null
        Game game = new Game(
                gamePhase: Game.GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE): new IndividualGameState(wordPhrase: "SETUP"),
                        (PTWO): new IndividualGameState(wordPhrase: "SETUP"),
                ]
        )

        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testPlayingToRematchSingleWinner() {

    }

    @Test
    public void testRematchToRematch() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Rematch, rematchId: "")
        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testRematchToRematched() {
        assert transitionEngine.gameScorer == null

        Game game = new Game(gamePhase: Game.GamePhase.Rematch, rematchId: "XYZ")
        Game saved = new Game()
        transitionEngine.gameRepository = [
                save: {
                    Game it ->
                        assert it == game
                        assert it.gamePhase == Game.GamePhase.Rematched
                        return saved
                }
        ] as GameRepository
        assert saved == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testRematchedToRematched() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Rematched)
        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }

    @Test
    public void testDeclinedToDeclined() {
        assert transitionEngine.gameRepository == null
        assert transitionEngine.gameScorer == null
        Game game = new Game(gamePhase: Game.GamePhase.Declined)
        assert game == transitionEngine.evaluateGamePhaseForGame(game)
    }
}
