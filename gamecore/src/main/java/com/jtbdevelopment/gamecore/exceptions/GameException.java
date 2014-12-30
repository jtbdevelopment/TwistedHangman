package com.jtbdevelopment.gamecore.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:40 AM
 */
public abstract class GameException extends RuntimeException {
    public GameException(final String s) {
        super(s);
    }
}
