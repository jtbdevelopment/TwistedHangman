package com.jtbdevelopment.TwistedHangman.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes;
import com.jtbdevelopment.core.spring.jackson.JacksonModuleCustomization;
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes;
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame;
import org.springframework.stereotype.Component;

/**
 * Date: 2/8/15 Time: 4:08 PM
 */
@Component
public class TwistedHangmanJacksonRegistration implements JacksonModuleCustomization {

  @Override
  public void customizeModule(final SimpleModule module) {
    module.addAbstractTypeMapping(GameSpecificPlayerAttributes.class,
        TwistedHangmanPlayerAttributes.class);
    module.addAbstractTypeMapping(MaskedMultiPlayerGame.class, THMaskedGame.class);
    module.addAbstractTypeMapping(com.jtbdevelopment.games.state.masking.MaskedGame.class,
        THMaskedGame.class);
    module.registerSubtypes(THMaskedGame.class);
  }

}
