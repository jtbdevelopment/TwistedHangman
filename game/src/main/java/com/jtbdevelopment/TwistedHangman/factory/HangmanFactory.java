package com.jtbdevelopment.TwistedHangman.factory;

import com.jtbdevelopment.TwistedHangman.game.HangmanGame;

/**
 * Date: 10/28/14
 * Time: 9:48 PM
 */
public interface HangmanFactory {
    public HangmanGame generateGame(final HangmanGameType type);

    public enum HangmanGameType {
        BasicGame,
        ThievingGame
    }
}
