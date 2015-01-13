package com.jtbdevelopment.TwistedHangman.exceptions.system

import com.jtbdevelopment.games.exceptions.GameSystemException

/**
 * Date: 1/13/15
 * Time: 6:41 PM
 */
class PreMadePuzzleFinderException extends GameSystemException {
    public final static String NO_GAMES_FOUND = 'No pre-made games were found.'
    public final static String GAME_NOT_LOADED = 'Pre-made game failed to load.'

    public PreMadePuzzleFinderException(final String message) {
        super(message)
    }
}
