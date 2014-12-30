package com.jtbdevelopment.gamecore.security.facebook

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.TwistedHangmanPlayerRepository
import com.jtbdevelopment.gamecore.players.ManualPlayer
import com.jtbdevelopment.gamecore.players.friendfinder.SourceBasedFriendFinder
import org.springframework.social.facebook.api.*

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

    void testFindFriends() {
        def R1 = new Reference("1")
        def R2 = new Reference("2")
        def R3 = new Reference("4")
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
                },
                fetchConnections: {
                    String start, String type, Class ret, String[] va ->
                        assert start == "me"
                        assert type == "invitable_friends"
                        assert ret == org.springframework.social.facebook.api.Reference.class

                        return new PagedList<org.springframework.social.facebook.api.Reference>(
                                [
                                        R1,
                                        R2,
                                        R3,
                                ],
                                new PagingParameters(0, 0, 0, 0),
                                new PagingParameters(0, 0, 0, 0)
                        )
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
        ] as TwistedHangmanPlayerRepository

        friendFinder.playerRepository = repo
        friendFinder.facebook = facebook


        def friends = friendFinder.findFriends(PONE)
        assert friends == [
                (SourceBasedFriendFinder.FRIENDS_KEY)          : [PTWO, PFOUR, PINACTIVE1] as Set,
                (SourceBasedFriendFinder.NOT_FOUND_KEY)        : [] as Set,
                (SourceBasedFriendFinder.INVITABLE_FRIENDS_KEY): [R1, R2, R3] as Set
        ]
    }
}
