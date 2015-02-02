package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributesFactory
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 2/2/15
 * Time: 5:37 PM
 */
@Component
@CompileStatic
class TwistedHangmanPlayerAttributesFactory implements GameSpecificPlayerAttributesFactory {
    @Override
    GameSpecificPlayerAttributes newPlayerAttributes() {
        return new TwistedHangmanPlayerAttributes()
    }

    @Override
    GameSpecificPlayerAttributes newManualPlayerAttributes() {
        return new TwistedHangmanPlayerAttributes()
    }

    @Override
    GameSpecificPlayerAttributes newSystemPlayerAttributes() {
        return null
    }
}
