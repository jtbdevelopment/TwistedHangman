package com.jtbdevelopment.TwistedHangman.game.multiplayer;

import com.jtbdevelopment.TwistedHangman.game.Player;

import java.util.Map;
import java.util.Set;

/**
 * Date: 10/29/14
 * Time: 7:11 PM
 */
public interface HeadToHeadHangmanGame {
    public String getId();

    //  Order
    public Set<Player> getPlayers();

    public PlayMode getPlayMode();

    public Map<Player, Integer> getPlayerScores();

    public enum PlayMode {
        Alternating,
        HeadToHead
    }


}
