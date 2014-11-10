package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/7/14
 * Time: 6:57 AM
 */
public class RandomCannedGameFinderException extends TwistedHangmanSystemException {
    public final static String NO_GAMES_FOUND = "No pre-made games were found.";
    public final static String GAME_NOT_LOADED = "Pre-made game failed to load.";

    public RandomCannedGameFinderException(final String message) {
        super(message);
    }
}
