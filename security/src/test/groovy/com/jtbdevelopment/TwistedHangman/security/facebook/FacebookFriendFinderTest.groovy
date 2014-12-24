package com.jtbdevelopment.TwistedHangman.security.facebook

import com.jtbdevelopment.TwistedHangman.players.ManualPlayer

/**
 * Date: 12/24/14
 * Time: 2:51 PM
 */
class FacebookFriendFinderTest extends GroovyTestCase {
    FacebookFriendFinder friendFinder = new FacebookFriendFinder()

    void testHandlesSource() {
        assert friendFinder.handlesSource("facebook")
        assert !friendFinder.handlesSource(ManualPlayer.MANUAL_SOURCE)
        assert !friendFinder.handlesSource("twitter")
    }

    void testFindFriends() {

    }
}
