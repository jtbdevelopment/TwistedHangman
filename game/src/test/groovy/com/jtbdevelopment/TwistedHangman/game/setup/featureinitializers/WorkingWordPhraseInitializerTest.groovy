package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 9:05 PM
 */
class WorkingWordPhraseInitializerTest extends GroovyTestCase {
    WorkingWordPhraseInitializer initializer = new WorkingWordPhraseInitializer()

    @Test
    public void testInitSimplePhrase() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "cat".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "___"
    }

    @Test
    public void testInitPunctuation() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "'cat'".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "'___'"
    }

    @Test
    public void testNumerics() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        gameState.wordPhrase = "1 cat".toCharArray()
        initializer.initializeForPhrase(gameState)
        assert gameState.workingWordPhraseString == "1 ___"
    }
}
