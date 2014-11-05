package com.jtbdevelopment.TwistedHangman.exceptions;

/**
 * Date: 11/5/14
 * Time: 6:51 AM
 */
public class StealingOnFinalPenaltyException extends TwistedHangmanException {
    public static final String CANT_STEAL_ON_FINAL_PENALTY_ERROR = "Can't steal a letter with only one penalty left.";

    public StealingOnFinalPenaltyException() {
        super(CANT_STEAL_ON_FINAL_PENALTY_ERROR);
    }
}