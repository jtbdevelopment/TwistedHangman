package com.jtbdevelopment.TwistedHangman.game.setup

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:11 AM
 */
@CompileStatic
interface PhraseFeatureInitializer {
      void initializeForPhrase(final IndividualGameState gameState)
}
