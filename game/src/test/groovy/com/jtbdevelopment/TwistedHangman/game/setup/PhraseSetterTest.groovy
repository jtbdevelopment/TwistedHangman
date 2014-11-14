package com.jtbdevelopment.TwistedHangman.game.setup

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState

/**
 * Date: 11/7/14
 * Time: 6:48 AM
 */
class PhraseSetterTest extends GroovyTestCase {
    private static String inputWordPhrase = "Bohemian Rhapsody"
    private static String inputCategory = "Song"
    private static String expectedWordPhrase = inputWordPhrase.toUpperCase()
    private static String expectedCategory = inputCategory.toUpperCase()

    PhraseSetter phraseSetter = new PhraseSetter()


    public void testPhraseSetting() {
        IndividualGameState gameState = new IndividualGameState([] as Set)
        boolean initCalled = false
        phraseSetter.phraseFeatureInitializers = [
                [initializeForPhrase: {
                    IndividualGameState state ->
                        //  Check here that everything is set before inits are called
                        assert state == gameState
                        assert state.category == expectedCategory
                        assert state.wordPhraseString == expectedWordPhrase
                        initCalled = true
                }] as PhraseFeatureInitializer
        ]

        phraseSetter.setWordPhrase(gameState, inputWordPhrase, inputCategory)
        assert initCalled
    }

}
