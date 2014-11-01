package com.jtbdevelopment.TwistedHangman.game.multiplayer;

import com.jtbdevelopment.TwistedHangman.game.Player;
import com.jtbdevelopment.TwistedHangman.game.PlayerGameState;

import java.util.Map;

/**
 * Date: 10/29/14
 * Time: 7:19 PM
 */
public abstract interface PlayerHangmanGame {

    public String getId();

    public int getVersion();

    public boolean isPaused();

    public GamePhase getGamePhase();

    public Map<Player, PlayerGameState> getPlayerGameStates();

    public enum GamePhase {
        Challenge,  /*  Agreement from initial players  */
        Setup, /*  Setting word phrases  */
        Playing,
        RematchOption,  /*  Option to continue to a new game  */
        Closed,
        Discontinued,  /*  Challenge was rejected  */
    }

}
