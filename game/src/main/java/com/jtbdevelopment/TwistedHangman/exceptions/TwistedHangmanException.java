package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:40 AM
 */
public abstract class TwistedHangmanException extends RuntimeException {
    public TwistedHangmanException() {
    }

    public TwistedHangmanException(final String s) {
        super(s);
    }

    public TwistedHangmanException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public TwistedHangmanException(final Throwable throwable) {
        super(throwable);
    }

    public TwistedHangmanException(final String s, final Throwable throwable, final boolean b, final boolean b1) {
        super(s, throwable, b, b1);
    }
}
