package com.jtbdevelopment.TwistedHangman.game.state

import org.junit.Test

/**
 * Date: 11/2/2014
 * Time: 9:22 PM
 */
class HangmanGameStateTest extends GroovyTestCase {
    @Test
    public void testInitialGameStateWithDefaultFeatures() {
        HangmanGameState gameState = new HangmanGameState("cat".toCharArray(), "xxx".toCharArray(), "animal", 5)
        assert !gameState.gameOver
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters.empty
        assert gameState.workingWordPhraseString == "xxx"
        assert gameState.wordPhraseString == "cat"
        assert gameState.maxPenalties == 5
        assert gameState.penalties == 0
        assert gameState.category == "animal"
        assert gameState.moveCount == 0
        assert gameState.penaltiesRemaining == 5
        assert gameState.featureData.isEmpty()
        assert gameState.features.empty
    }

    @Test
    public void testInitialGameStateWithFeatures() {
        def features = [HangmanGameFeatures.Thieving, HangmanGameFeatures.ThievingCountTracking] as Set
        HangmanGameState gameState = new HangmanGameState(
                "CaT".toCharArray(),
                "_x_".toCharArray(),
                "animal",
                7,
                features)
        assert !gameState.gameOver
        assert !gameState.gameLost
        assert !gameState.gameWon
        assert gameState.badlyGuessedLetters.empty
        assert gameState.guessedLetters.empty
        assert gameState.workingWordPhraseString == "_x_"
        assert gameState.wordPhraseString == "CaT"
        assert gameState.maxPenalties == 7
        assert gameState.penalties == 0
        assert gameState.category == "animal"
        assert gameState.moveCount == 0
        assert gameState.penaltiesRemaining == 7
        assert gameState.featureData.isEmpty()
        assert gameState.features == features
    }

}
