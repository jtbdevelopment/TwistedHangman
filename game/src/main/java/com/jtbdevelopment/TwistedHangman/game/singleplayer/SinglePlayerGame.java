package com.jtbdevelopment.TwistedHangman.game.singleplayer;

import com.jtbdevelopment.TwistedHangman.game.Player;

/**
 * Date: 11/2/14
 * Time: 4:21 PM
 */
public interface SinglePlayerGame {
    String getId();

    Player getPlayer();

    int getScore();


}
