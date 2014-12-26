package com.jtbdevelopment.TwistedHangman.security.spring.security

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.ManualPlayer

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
                        assert source == ManualPlayer.MANUAL_SOURCE
                        assert username == PTWO.sourceId
                        PTWO
                }
        ] as PlayerRepository
        userDetailsService.playerRepository = repo

        PlayerUserDetails d = userDetailsService.loadUserByUsername(PTWO.sourceId)
        assert d.effectiveUser == PTWO
        assert d.sessionUser == PTWO
    }

    void testNoLoadUserByUsername() {
        def repo = [
                findBySourceAndSourceId: {
                    String source, String username ->
                        assert source == ManualPlayer.MANUAL_SOURCE
                        assert username == PTWO.sourceId
                        null
                }
        ] as PlayerRepository
        userDetailsService.playerRepository = repo

        assert userDetailsService.loadUserByUsername(PTWO.sourceId) == null
    }
}