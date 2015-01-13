package com.jtbdevelopment.TwistedHangman.exceptions.input

import com.jtbdevelopment.games.exceptions.GameInputException

/**
 * Date: 1/13/15
 * Time: 6:54 PM
 */
class GameOverException extends GameInputException {
    static final String GAME_OVER_ERROR = 'Game is already over.'

    GameOverException() {
        super(GAME_OVER_ERROR)
    }
}
