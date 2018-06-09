package com.jtbdevelopment.TwistedHangman.json;

import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanPlayerAttributes;
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes;
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 2/8/15 Time: 4:29 PM
 */
public class TwistedHangmanJacksonRegistrationTest {

  @Test
  public void testCustomizeModule() {
    TwistedHangmanJacksonRegistration registration = new TwistedHangmanJacksonRegistration();
    SimpleModule module = Mockito.mock(SimpleModule.class);
    registration.customizeModule(module);
    verify(module).addAbstractTypeMapping(GameSpecificPlayerAttributes.class,
        TwistedHangmanPlayerAttributes.class);
    verify(module).addAbstractTypeMapping(MaskedMultiPlayerGame.class, THMaskedGame.class);
    verify(module).addAbstractTypeMapping(com.jtbdevelopment.games.state.masking.MaskedGame.class,
        THMaskedGame.class);
  }

}
