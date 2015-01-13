package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:12 AM
 */
@CompileStatic
interface GameInitializer {
      void initializeGame(final Game game)
}