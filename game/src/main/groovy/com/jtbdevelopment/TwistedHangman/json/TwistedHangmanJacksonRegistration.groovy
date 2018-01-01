package com.jtbdevelopment.TwistedHangman.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame
import com.jtbdevelopment.spring.jackson.JacksonModuleCustomization
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 2/8/15
 * Time: 4:08 PM
 */
@Component
@CompileStatic
class TwistedHangmanJacksonRegistration implements JacksonModuleCustomization {
    @Override
    void customizeModule(final SimpleModule module) {
        module.addAbstractTypeMapping(GameSpecificPlayerAttributes.class, TwistedHangmanPlayerAttributes.class)
        module.addAbstractTypeMapping(MaskedMultiPlayerGame.class, MaskedGame.class)
        module.addAbstractTypeMapping(com.jtbdevelopment.games.state.masking.MaskedGame.class, MaskedGame.class)
        module.registerSubtypes(MaskedGame.class)
    }
}
