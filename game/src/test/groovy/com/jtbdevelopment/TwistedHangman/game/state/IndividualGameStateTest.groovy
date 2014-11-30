package com.jtbdevelopment.TwistedHangman.game.state
/**
 * Date: 11/2/2014
 * Time: 9:22 PM
 */
class IndividualGameStateTest extends GroovyTestCase {

    public void testGameOverWhenGameNotSetYet() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        assert !gameState.puzzleSolved
        assert !gameState.playerHung
        assert !gameState.puzzleOver
    }

    public void testInitialGameStateWithDefaultFeatures() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "cat".toCharArray()
        gameState.workingWordPhrase = "xxx".toCharArray()
        gameState.category = "animal"
        gameState.maxPenalties = 5
        assert !gameState.puzzleOver
        assert !gameState.playerHung
        assert !gameState.puzzleSolved
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


    public void testInitialGameStateWithFeatures() {
        def features = [GameFeature.Thieving, GameFeature.ThievingCountTracking] as Set
        IndividualGameState gameState = new IndividualGameState(features)
        gameState.wordPhrase = "CaT".toCharArray()
        gameState.workingWordPhrase = "_x_".toCharArray()
        gameState.category = "animal"
        gameState.maxPenalties = 7
        assert !gameState.puzzleOver
        assert !gameState.playerHung
        assert !gameState.puzzleSolved
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


    public void testToString() {
        assert new IndividualGameState(
                badlyGuessedLetters: [(char) 'A'] as SortedSet,
                category: "CATEGORY",
                features: [GameFeature.SingleWinner] as Set,
                featureData: [(GameFeature.DrawFace): "da"],
                guessedLetters: [(char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 5,
                penalties: 4,
                blanksRemaining: 4,
                wordPhrase: "WORD",
                workingWordPhrase: "W___D",).toString() == "IndividualGameState{category='CATEGORY', wordPhrase=[W, O, R, D], workingWordPhrase=[W, _, _, _, D], maxPenalties=10, moveCount=5, penalties=4, features=[SingleWinner], badlyGuessedLetters=[A], guessedLetters=[B], featureData=[DrawFace:da], blanksRemaining=4}"
    }
}
