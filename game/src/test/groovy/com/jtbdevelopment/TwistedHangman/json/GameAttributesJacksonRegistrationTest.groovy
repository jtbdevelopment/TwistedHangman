package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes

/**
 * Date: 2/8/15
 * Time: 4:29 PM
 */
class GameAttributesJacksonRegistrationTest extends GroovyTestCase {
    void testCustomizeModule() {
        GameAttributesJacksonRegistration registration = new GameAttributesJacksonRegistration()
        boolean registered = false
        def module = [
                addAbstractTypeMapping: {
                    Class iface, Class impl ->
                        assert GameSpecificPlayerAttributes.class.is(iface)
                        assert TwistedHangmanPlayerAttributes.class.is(impl)
                        registered = true
                        return null
                }
        ] as SimpleModule
        registration.customizeModule(module)
        assert registered

    }
}
