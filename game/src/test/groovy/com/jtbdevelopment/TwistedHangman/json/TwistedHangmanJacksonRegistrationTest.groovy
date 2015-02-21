package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.games.masked.MaskedMultiPlayerGame
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes

/**
 * Date: 2/8/15
 * Time: 4:29 PM
 */
class TwistedHangmanJacksonRegistrationTest extends GroovyTestCase {
    void testCustomizeModule() {
        TwistedHangmanJacksonRegistration registration = new TwistedHangmanJacksonRegistration()
        boolean registeredGameAttributes = false
        boolean registeredMaskedGame = false
        def module = [
                addAbstractTypeMapping: {
                    Class iface, Class impl ->
                        if (GameSpecificPlayerAttributes.class.is(iface)) {
                            assert TwistedHangmanPlayerAttributes.class.is(impl)
                            registeredGameAttributes = true
                            return null
                        }
                        if (MaskedMultiPlayerGame.class.is(iface)) {
                            assert MaskedGame.class.is(impl)
                            registeredMaskedGame = true
                            return null
                        }
                        fail('unexpected attributes')
                }
        ] as SimpleModule
        registration.customizeModule(module)
        assert registeredGameAttributes
        assert registeredMaskedGame
    }
}
