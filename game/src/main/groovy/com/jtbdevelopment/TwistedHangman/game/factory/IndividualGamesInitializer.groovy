package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:13 AM
 *
 * Note - having this outside game initializer seemed to be a mistake and did not move to core
 */
@CompileStatic
interface IndividualGamesInitializer {
      void initializeIndividualGameStates(final Game game)
}
