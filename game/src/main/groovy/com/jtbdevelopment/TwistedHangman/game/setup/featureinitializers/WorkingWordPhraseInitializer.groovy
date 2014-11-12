package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseFeatureInitializer
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:41 PM
 */
@Component
@CompileStatic
class WorkingWordPhraseInitializer implements PhraseFeatureInitializer {
    @Override
    void initializeForPhrase(final IndividualGameState gameState) {
        int length = gameState.wordPhrase.length
        char[] workingWordPhrase = new char[length]

        for (int i = 0; i < length; ++i) {
            if (Character.isLetter(gameState.wordPhrase[i])) {
                workingWordPhrase[i] = '_';
            } else {
                workingWordPhrase[i] = gameState.wordPhrase[i];
            }
        }
        gameState.workingWordPhrase = workingWordPhrase
    }
}
