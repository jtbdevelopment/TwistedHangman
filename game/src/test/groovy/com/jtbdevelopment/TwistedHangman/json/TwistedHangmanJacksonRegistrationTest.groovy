package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame

/**
 * Date: 2/8/15
 * Time: 4:29 PM
 */
class TwistedHangmanJacksonRegistrationTest extends GroovyTestCase {
    void testCustomizeModule() {
        TwistedHangmanJacksonRegistration registration = new TwistedHangmanJacksonRegistration()
        boolean registeredGameAttributes = false
        boolean registeredMaskedGame = false
        boolean registeredBaseMaskedGame = false;
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
                        if (com.jtbdevelopment.games.state.masking.MaskedGame.class.is(iface)) {
                            assert MaskedGame.class.is(impl)
                            registeredBaseMaskedGame = true
                            return null
                        }
                        fail('unexpected attributes')
                }
        ] as SimpleModule
        registration.customizeModule(module)
        assert registeredGameAttributes
        assert registeredMaskedGame
        assert registeredBaseMaskedGame
    }
}
