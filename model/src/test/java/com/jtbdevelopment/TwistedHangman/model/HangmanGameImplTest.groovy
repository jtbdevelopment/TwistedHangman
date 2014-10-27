package com.jtbdevelopment.TwistedHangman.model

/**
 * Date: 10/25/2014
 * Time: 6:28 PM
 */

//  TODO - test exceptions in constructor and guesser

class HangmanGameImplTest extends AbstractHangmanGameTest<HangmanGameImpl> {
    @Override
    protected HangmanGameImpl makeGame(final String wordPhrase, final String category, final int maxPenalties) {
        return new HangmanGameImpl(wordPhrase, category, maxPenalties)
    }
}
