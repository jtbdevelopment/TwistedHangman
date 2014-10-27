package com.jtbdevelopment.TwistedHangman.model;

/**
 * Date: 10/26/2014
 * Time: 8:37 PM
 */
public interface ThievingHangmanGame extends HangmanGame {
    public static final String STEALING_KNOWN_LETTER_ERROR = "Letter already known at position.";
    public static final String CANT_STEAL_ON_FINAL_PENALTY_ERROR = "Can't steal a letter with only one move to losing.";

    public void stealLetter(int position);

    public boolean[] getStolenLetterMarkers();

    public int getStolenLettersCount();
}
