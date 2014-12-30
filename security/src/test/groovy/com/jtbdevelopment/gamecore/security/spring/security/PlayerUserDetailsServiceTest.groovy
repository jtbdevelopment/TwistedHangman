package com.jtbdevelopment.gamecore.security.spring.security

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.TwistedHangmanPlayerRepository
import com.jtbdevelopment.gamecore.mongo.players.MongoManualPlayer

/**
 * Date: 12/24/14
 * Time: 4:48 PM
 */
class PlayerUserDetailsServiceTest extends TwistedHangmanTestCase {
    PlayerUserDetailsService userDetailsService = new PlayerUserDetailsService()

    void testLoadUserByUsername() {
        def repo = [
                findBySourceAndSourceId: {
                    String source, String username ->
                        assert source == MongoManualPlayer.MANUAL_SOURCE
                        assert username == PTWO.sourceId
                        PTWO
                }
        ] as TwistedHangmanPlayerRepository
        userDetailsService.playerRepository = repo

        PlayerUserDetails d = userDetailsService.loadUserByUsername(PTWO.sourceId)
        assert d.effectiveUser == PTWO
        assert d.sessionUser == PTWO
    }

    void testNoLoadUserByUsername() {
        def repo = [
                findBySourceAndSourceId: {
                    String source, String username ->
                        assert source == MongoManualPlayer.MANUAL_SOURCE
                        assert username == PTWO.sourceId
                        null
                }
        ] as TwistedHangmanPlayerRepository
        userDetailsService.playerRepository = repo

        assert userDetailsService.loadUserByUsername(PTWO.sourceId) == null
    }
}
