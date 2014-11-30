package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 11/6/14
 * Time: 9:05 PM
 */
class WorkingWordPhraseInitializerTest extends GroovyTestCase {
    WorkingWordPhraseInitializer initializer = new WorkingWordPhraseInitializer()


    public void testInitSimplePhrase() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "cat".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "___"
        assert gameState.blanksRemaining == 3;
    }


    public void testInitPunctuation() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "'cat'".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "'___'"
        assert gameState.blanksRemaining == 3;
    }


    public void testNumerics() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "1 cat".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "1 ___"
        assert gameState.blanksRemaining == 3;
    }
}
