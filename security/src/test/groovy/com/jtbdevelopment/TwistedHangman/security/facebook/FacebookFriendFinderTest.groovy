package com.jtbdevelopment.TwistedHangman.security.facebook

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.ManualPlayer
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FriendOperations
import org.springframework.social.facebook.api.PagedList
import org.springframework.social.facebook.api.PagingParameters

/**
 * Date: 12/24/14
 * Time: 2:51 PM
 */
class FacebookFriendFinderTest extends TwistedHangmanTestCase {
    FacebookFriendFinder friendFinder = new FacebookFriendFinder()

    void testHandlesSource() {
        assert friendFinder.handlesSource("facebook")
        assert !friendFinder.handlesSource(ManualPlayer.MANUAL_SOURCE)
        assert !friendFinder.handlesSource("twitter")
    }
/*
                friendOperations: {
                    getFriends: {
                        return new PagedList<Reference>(
                                [PTWO.sourceId, PFOUR.source, PINACTIVE1.sourceId],
                                new PagingParameters(0, 0, 0, 0),
                                new PagingParameters(0, 0, 0, 0)
                        )
                    ] as FriendOperations
                    }

 */

    void testFindFriends() {
        def facebook = [
                friendOperations: {
                    return [
                            getFriends: {
                                return new PagedList<org.springframework.social.facebook.api.Reference>(
                                        [
                                                new org.springframework.social.facebook.api.Reference(PTWO.sourceId),
                                                new org.springframework.social.facebook.api.Reference(PFOUR.sourceId),
                                                new org.springframework.social.facebook.api.Reference(PINACTIVE1.sourceId),
                                        ],
                                        new PagingParameters(0, 0, 0, 0),
                                        new PagingParameters(0, 0, 0, 0)
                                )
                            }
                    ] as FriendOperations
                }
        ] as Facebook
        def repo = [
                findBySourceAndSourceId: {
                    String source, String sourceId ->
                        assert source == "facebook"
                        switch (sourceId) {
                            case PTWO.sourceId:
                                return PTWO
                            case PFOUR.sourceId:
                                return PFOUR
                            case PINACTIVE1.sourceId:
                                return PINACTIVE1
                        }
                        fail("Unknown sourceId")
                }
        ] as PlayerRepository

        friendFinder.playerRepository = repo
        friendFinder.facebook = facebook

        assert friendFinder.findFriends(PONE) == [PTWO, PFOUR, PINACTIVE1] as Set
    }
}
