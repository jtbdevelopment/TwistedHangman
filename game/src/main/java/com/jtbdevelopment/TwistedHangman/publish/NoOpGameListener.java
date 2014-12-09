package com.jtbdevelopment.TwistedHangman.publish;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.players.Player;
import org.springframework.stereotype.Component;

/**
 * Date: 12/8/14
 * Time: 7:51 PM
 * <p>
 * Dummy until we have real one
 */
@Component
public class NoOpGameListener implements GameListener {
    @Override
    public void gameChanged(final Game game, final Player initiatingPlayer) {

    }
}
