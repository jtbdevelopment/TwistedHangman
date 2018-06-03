package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import org.junit.Test

/**
 * Date: 11/8/2014
 * Time: 6:57 PM
 */
class GameScorerImplTest extends TwistedHangmanTestCase {
    GameScorerImpl gameScorer = new GameScorerImpl()

    @Test
    void testScoreForMultiWinMultiPlayerSystemGame() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id)  : [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTWO.id)  : [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTHREE.id): [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PFOUR.id) : [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerRunningScores[PONE.id] == 0
        assert game.playerRunningScores[PTWO.id] == -1
        assert game.playerRunningScores[PTHREE.id] == 5
        assert game.playerRunningScores[PFOUR.id] == -1

        assert game.playerRoundScores[PONE.id] == -1
        assert game.playerRoundScores[PTWO.id] == 1
        assert game.playerRoundScores[PTHREE.id] == 1
        assert game.playerRoundScores[PFOUR.id] == -1
    }


    @Test
    void testScoreForMultiWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE.id
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id) : [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTWO.id) : [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PFOUR.id): [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerRunningScores[PONE.id] == 0
        assert game.playerRunningScores[PTWO.id] == -1
        assert game.playerRunningScores[PTHREE.id] == 5
        assert game.playerRunningScores[PFOUR.id] == -1

        assert game.playerRoundScores[PONE.id] == -1
        assert game.playerRoundScores[PTWO.id] == 1
        assert game.playerRoundScores[PTHREE.id] == 1
        assert game.playerRoundScores[PFOUR.id] == -1
    }


    @Test
    void testScoreForMultiWinTwoPlayerHeadToHead() {
        Game game = new Game()
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2]
        game.playerRoundScores = [(PONE.id): 0, (PTWO.id): 0]
        game.solverStates = [
                (PONE.id): [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTWO.id): [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerRunningScores[PONE.id] == 2
        assert game.playerRunningScores[PTWO.id] == -1

        assert game.playerRoundScores[PONE.id] == 1
        assert game.playerRoundScores[PTWO.id] == 1
    }


    @Test
    void testScoreForSingleWinMultiPlayerSystemGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.playerRoundScores = [(PONE.id): 0, (PTWO.id): 0, (PTHREE.id): 0, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id)  : [isPuzzleSolved: { false }, isPlayerHung: { false }, isPuzzleOver: {
                    false
                }] as IndividualGameState,
                (PTWO.id)  : [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTHREE.id): [isPuzzleSolved: { false }, isPlayerHung: { false }, isPuzzleOver: {
                    false
                }] as IndividualGameState,
                (PFOUR.id) : [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PTWO.id
        assert game.playerRunningScores[PONE.id] == 1
        assert game.playerRunningScores[PTWO.id] == -1
        assert game.playerRunningScores[PTHREE.id] == 4
        assert game.playerRunningScores[PFOUR.id] == -1


        assert game.playerRoundScores[PONE.id] == 0
        assert game.playerRoundScores[PTWO.id] == 1
        assert game.playerRoundScores[PTHREE.id] == 0
        assert game.playerRoundScores[PFOUR.id] == -1
    }


    @Test
    void testScoreForSingleWinMultiPlayerAlternatingGame() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO, PTHREE, PFOUR]
        game.wordPhraseSetter = PTHREE.id
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2, (PTHREE.id): 4, (PFOUR.id): 0]
        game.playerRoundScores = [(PONE.id): 0, (PTWO.id): 0, (PTHREE.id): 0, (PFOUR.id): 0]
        game.solverStates = [
                (PONE.id) : [isPuzzleSolved: { false }, isPlayerHung: { false }, isPuzzleOver: {
                    false
                }] as IndividualGameState,
                (PTWO.id) : [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PFOUR.id): [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PTWO.id
        assert game.playerRunningScores[PONE.id] == 1
        assert game.playerRunningScores[PTWO.id] == -1
        assert game.playerRunningScores[PTHREE.id] == 4
        assert game.playerRunningScores[PFOUR.id] == -1

        assert game.playerRoundScores[PONE.id] == 0
        assert game.playerRoundScores[PTWO.id] == 1
        assert game.playerRoundScores[PTHREE.id] == 0
        assert game.playerRoundScores[PFOUR.id] == -1
    }


    @Test
    void testScoreForSingleWinTwoPlayerHeadToHead() {
        Game game = new Game()
        game.features.add(GameFeature.SingleWinner)
        game.players = [PONE, PTWO]
        game.wordPhraseSetter = null
        game.playerRunningScores = [(PONE.id): 1, (PTWO.id): -2]
        game.playerRoundScores = [(PONE.id): 0, (PTWO.id): 0]
        game.solverStates = [
                (PONE.id): [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
                (PTWO.id): [isPuzzleSolved: { false }, isPlayerHung: { false }, isPuzzleOver: {
                    false
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == PONE.id
        assert game.playerRunningScores[PONE.id] == 2
        assert game.playerRunningScores[PTWO.id] == -2

        assert game.playerRoundScores[PONE.id] == 1
        assert game.playerRoundScores[PTWO.id] == 0
    }


    @Test
    void testScoreWinForSinglePlayerGame() {
        Game game = new Game()
        game.players = [PONE]
        game.wordPhraseSetter = TwistedHangmanSystemPlayerCreator.TH_PLAYER.id
        game.playerRunningScores = [(PONE.id): 1]
        game.solverStates = [
                (PONE.id): [isPuzzleSolved: { true }, isPlayerHung: { false }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerRunningScores[PONE.id] == 2

        assert game.playerRoundScores[PONE.id] == 1
    }


    @Test
    void testLossWinForSinglePlayerGame() {
        Game game = new Game()
        game.players = [PONE]
        game.wordPhraseSetter = TwistedHangmanSystemPlayerCreator.TH_PLAYER.id
        game.playerRunningScores = [(PONE.id): 1]
        game.solverStates = [
                (PONE.id): [isPuzzleSolved: { false }, isPlayerHung: { true }, isPuzzleOver: {
                    true
                }] as IndividualGameState,
        ]
        assert gameScorer.scoreGame(game).is(game)
        assert game.featureData[GameFeature.SingleWinner] == null
        assert game.playerRunningScores[PONE.id] == 0

        assert game.playerRoundScores[PONE.id] == -1
    }
}

