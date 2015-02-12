package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.spring.jackson.JacksonModuleCustomization
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 2/8/15
 * Time: 4:08 PM
 */
@Component
@CompileStatic
class GameAttributesJacksonRegistration implements JacksonModuleCustomization {
    @Override
    void customizeModule(final SimpleModule module) {
        module.addAbstractTypeMapping(GameSpecificPlayerAttributes.class, TwistedHangmanPlayerAttributes.class)
        module.addAbstractTypeMapping(Player.class, MongoPlayer.class)
    }
}
