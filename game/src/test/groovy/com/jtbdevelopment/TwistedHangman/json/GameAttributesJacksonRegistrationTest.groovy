package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.players.Player

/**
 * Date: 2/8/15
 * Time: 4:29 PM
 */
class GameAttributesJacksonRegistrationTest extends GroovyTestCase {
    void testCustomizeModule() {
        GameAttributesJacksonRegistration registration = new GameAttributesJacksonRegistration()
        boolean registeredGameAttributes = false
        boolean registerPlayer = false
        def module = [
                addAbstractTypeMapping: {
                    Class iface, Class impl ->
                        if (GameSpecificPlayerAttributes.class.is(iface)) {
                            assert TwistedHangmanPlayerAttributes.class.is(impl)
                            registeredGameAttributes = true
                        }
                        if (Player.class.is(iface)) {
                            assert MongoPlayer.class.is(impl)
                            registerPlayer = true
                        }
                        return null
                }
        ] as SimpleModule
        registration.customizeModule(module)
        assert registeredGameAttributes
        assert registerPlayer
    }
}
