package com.jtbdevelopment.TwistedHangman.game.setup

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:37 PM
 */
@CompileStatic
@Component
class PhraseSetter {
    @Autowired
    List<PhraseFeatureInitializer> phraseFeatureInitializers

    public void setWordPhrase(final IndividualGameState gameState, final String wordPhrase, final String category) {
        gameState.category = category.toUpperCase()
        gameState.wordPhrase = wordPhrase.toUpperCase().toCharArray()
        phraseFeatureInitializers.each { PhraseFeatureInitializer initializer -> initializer.initializeForPhrase(gameState) }
    }
}
