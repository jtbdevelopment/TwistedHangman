package com.jtbdevelopment.TwistedHangman.model
/**
 * Date: 10/25/14
 * Time: 8:21 PM
 */
class ThievingHangmanGameGameImplTest extends AbstractThievingHangmanGameTest<ThievingHangmanGameImpl> {

    @Override
    protected ThievingHangmanGameImpl makeGame(final String wordPhrase, final String category, final int maxPenalties) {
        return new ThievingHangmanGameImpl(new HangmanGameImpl(wordPhrase, category, maxPenalties))
    }
}
