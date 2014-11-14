package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/8/2014
 * Time: 6:57 PM
 */
class GameScorerTest extends TwistedHangmanTestCase {
    GameScorer gameScorer = new GameScorer()


    public void testScoreForMultiWinMultiPlayerSystemGame() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id)  : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
                (PTWO.id)  : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTHREE.id): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR.id) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE.id] == 0
        assert game.playerScores[PTWO.id] == -1
        assert game.playerScores[PTHREE.id] == 5
        assert game.playerScores[PFOUR.id] == -1
    }


    public void testScoreForMultiWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
                (PTWO.id) : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR.id): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE.id] == 0
        assert game.playerScores[PTWO.id] == -1
        assert game.playerScores[PTHREE.id] == 5
        assert game.playerScores[PFOUR.id] == -1
    }


    public void testScoreForMultiWinTwoPlayerHeadToHead() {
        Game game = new Game()
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2]
        game.solverStates = [
                (PONE.id): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTWO.id): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE.id] == 2
        assert game.playerScores[PTWO.id] == -1
    }


    public void testScoreForSingleWinMultiPlayerSystemGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id)  : [isGameWon: { false }, isGameLost: { false }, isGameOver: {
                    false
                }] as IndividualGameState,
                (PTWO.id)  : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTHREE.id): [isGameWon: { false }, isGameLost: { false }, isGameOver: {
                    false
                }] as IndividualGameState,
                (PFOUR.id) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PTWO
        assert game.playerScores[PONE.id] == 1
        assert game.playerScores[PTWO.id] == -1
        assert game.playerScores[PTHREE.id] == 4
        assert game.playerScores[PFOUR.id] == -1
    }


    public void testScoreForSingleWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id) : [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
                (PTWO.id) : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR.id): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PTWO
        assert game.playerScores[PONE.id] == 1
        assert game.playerScores[PTWO.id] == -1
        assert game.playerScores[PTHREE.id] == 4
        assert game.playerScores[PFOUR.id] == -1
    }


    public void testScoreForSingleWinTwoPlayerHeadToHead() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerScores = [(PONE.id): 1, (PTWO.id): -2]
        game.solverStates = [
                (PONE.id): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTWO.id): [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PONE
        assert game.playerScores[PONE.id] == 2
        assert game.playerScores[PTWO.id] == -2
    }


    public void testScoreWinForSinglePlayerGame() {
        Game game = new Game()
        game.players = [PONE.id]
        game.wordPhraseSetter = Player.SYSTEM_PLAYER
        game.playerScores = [(PONE.id): 1]
        game.solverStates = [
                (PONE.id): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE.id] == 2
    }


    public void testLossWinForSinglePlayerGame() {
        Game game = new Game()
        game.players = [PONE]
        game.wordPhraseSetter = Player.SYSTEM_PLAYER
        game.playerScores = [(PONE.id): 1]
        game.solverStates = [
                (PONE.id): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE.id] == 0
    }
}

