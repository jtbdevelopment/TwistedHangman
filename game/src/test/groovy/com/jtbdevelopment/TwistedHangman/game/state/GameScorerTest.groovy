package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/8/2014
 * Time: 6:57 PM
 */
class GameScorerTest extends TwistedHangmanTestCase {
    GameScorer gameScorer = new GameScorer()

    @Test
    public void testScoreForMultiWinMultiPlayerSystemGame() {
        Game game = new Game()
        Game savedGame = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerScores = [(PONE): 1, (PTWO): -2, (PTHREE): 4, (PFOUR): 0]
        game.solverStates = [
                (PONE)  : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
                (PTWO)  : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTHREE): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE] == 0
        assert game.playerScores[PTWO] == -1
        assert game.playerScores[PTHREE] == 5
        assert game.playerScores[PFOUR] == -1
    }

    @Test
    public void testScoreForMultiWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        Game savedGame = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE
        game.playerScores = [(PONE): 1, (PTWO): -2, (PTHREE): 4, (PFOUR): 0]
        game.solverStates = [
                (PONE) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
                (PTWO) : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE] == 0
        assert game.playerScores[PTWO] == -1
        assert game.playerScores[PTHREE] == 5
        assert game.playerScores[PFOUR] == -1
    }

    @Test
    public void testScoreForMultiWinTwoPlayerHeadToHead() {
        Game game = new Game()
        Game savedGame = new Game()
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerScores = [(PONE): 1, (PTWO): -2]
        game.solverStates = [
                (PONE): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTWO): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE] == 2
        assert game.playerScores[PTWO] == -1
    }

    @Test
    public void testScoreForSingleWinMultiPlayerSystemGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        Game savedGame = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerScores = [(PONE): 1, (PTWO): -2, (PTHREE): 4, (PFOUR): 0]
        game.solverStates = [
                (PONE)  : [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
                (PTWO)  : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTHREE): [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
                (PFOUR) : [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == PTWO
        assert game.playerScores[PONE] == 1
        assert game.playerScores[PTWO] == -1
        assert game.playerScores[PTHREE] == 4
        assert game.playerScores[PFOUR] == -1
    }

    @Test
    public void testScoreForSingleWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        Game savedGame = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE
        game.playerScores = [(PONE): 1, (PTWO): -2, (PTHREE): 4, (PFOUR): 0]
        game.solverStates = [
                (PONE) : [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
                (PTWO) : [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PFOUR): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == PTWO
        assert game.playerScores[PONE] == 1
        assert game.playerScores[PTWO] == -1
        assert game.playerScores[PTHREE] == 4
        assert game.playerScores[PFOUR] == -1
    }

    @Test
    public void testScoreForSingleWinTwoPlayerHeadToHead() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        Game savedGame = new Game()
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerScores = [(PONE): 1, (PTWO): -2]
        game.solverStates = [
                (PONE): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
                (PTWO): [isGameWon: { false }, isGameLost: { false }, isGameOver: { false }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == PONE
        assert game.playerScores[PONE] == 2
        assert game.playerScores[PTWO] == -2
    }


    @Test
    public void testScoreWinForSinglePlayerGame() {
        Game game = new Game()
        Game savedGame = new Game()
        game.players = [PONE]
        game.wordPhraseSetter = Player.SYSTEM_PLAYER
        game.playerScores = [(PONE): 1]
        game.solverStates = [
                (PONE): [isGameWon: { true }, isGameLost: { false }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE] == 2
    }

    @Test
    public void testLossWinForSinglePlayerGame() {
        Game game = new Game()
        Game savedGame = new Game()
        game.players = [PONE]
        game.wordPhraseSetter = Player.SYSTEM_PLAYER
        game.playerScores = [(PONE): 1]
        game.solverStates = [
                (PONE): [isGameWon: { false }, isGameLost: { true }, isGameOver: { true }] as IndividualGameState,
        ]
        gameScorer.gameRepository = [save: { assert it == game; savedGame }] as GameRepository
        assert gameScorer.scoreGame(game) == savedGame
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerScores[PONE] == 0
    }
}

