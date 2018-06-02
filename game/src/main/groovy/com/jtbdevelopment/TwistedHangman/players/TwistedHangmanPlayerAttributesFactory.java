package com.jtbdevelopment.TwistedHangman.players;

import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes;
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributesFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 2/2/15 Time: 5:37 PM
 */
@Component
public class TwistedHangmanPlayerAttributesFactory implements GameSpecificPlayerAttributesFactory {

  @Override
  public GameSpecificPlayerAttributes newPlayerAttributes() {
    return new TwistedHangmanPlayerAttributes();
  }

  @Override
  public GameSpecificPlayerAttributes newManualPlayerAttributes() {
    return new TwistedHangmanPlayerAttributes();
  }

  @Override
  public GameSpecificPlayerAttributes newSystemPlayerAttributes() {
    return null;
  }

}
