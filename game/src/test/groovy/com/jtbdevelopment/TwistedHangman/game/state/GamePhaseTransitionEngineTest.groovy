package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState
import com.jtbdevelopment.games.state.scoring.GameScorer
import org.bson.types.ObjectId
import org.junit.Test
import org.mockito.Mockito

/**
 * Date: 11/9/14
 * Time: 9:33 AM
 */
class GamePhaseTransitionEngineTest extends TwistedHangmanTestCase {
    private GameScorer gameScorer = Mockito.mock(GameScorer.class)
    private GamePhaseTransitionEngine transitionEngine = new GamePhaseTransitionEngine(gameScorer)


    @Test
    void testSinglePlayerChallengeTransitionsToPlaying() {
        Game game = new Game(
                gamePhase: GamePhase.Challenged,
                features: [GameFeature.SinglePlayer, GameFeature.SystemPuzzles] as Set,
                playerStates: [(PONE.id): PlayerState.Accepted],
                solverStates: [(PONE.id): new IndividualGameState(maxPenalties: 10, wordPhrase: "SET".toCharArray())]
        )

        assert game.is(transitionEngine.evaluateGame(game))
        assert game.gamePhase == GamePhase.Playing
    }


    @Test
    void testSetupToSetup() {
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


    @Test
    void testSetupToPlaying() {
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


    @Test
    void testPlayingToPlaying() {
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


    @Test
    void testPlayingToRematchMultipleWinners() {
        Game game = new Game(
                id: new ObjectId(),
                gamePhase: GamePhase.Playing,
                features: [] as Set,
                solverStates: [
                        (PONE.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTWO.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTHREE.id): new IndividualGameState(wordPhrase: "SETUP", penalties: IndividualGameState.BASE_PENALTIES)
                ]
        )
        Game scored = (Game) game.clone()
        Mockito.when(gameScorer.scoreGame(game)).thenReturn(scored)


        assert scored.is(transitionEngine.evaluateGame(game))
    }


    @Test
    void testPlayingToRematchSingleWinner() {
        Game game = new Game(
                id: new ObjectId(),
                gamePhase: GamePhase.Playing,
                features: [GameFeature.SingleWinner] as Set,
                solverStates: [
                        (PONE.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SETUP"),
                        (PTWO.id)  : new IndividualGameState(wordPhrase: "SETUP", workingWordPhrase: "SET__"),
                        (PTHREE.id): new IndividualGameState(wordPhrase: "SETUP", penalties: IndividualGameState.BASE_PENALTIES)
                ]
        )
        Game scored = (Game) game.clone()
        Mockito.when(gameScorer.scoreGame(game)).thenReturn(scored)

        assert scored.is(transitionEngine.evaluateGame(game))
    }
}
