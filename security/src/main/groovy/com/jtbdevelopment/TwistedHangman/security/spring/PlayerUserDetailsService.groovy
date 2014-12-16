package com.jtbdevelopment.TwistedHangman.security.spring

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.ManualPlayer
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * Date: 12/16/14
 * Time: 12:31 PM
 *
 * This is presumably only used by manual login where username = sourceId and source = MANUAL
 */
@Component
@CompileStatic
class PlayerUserDetailsService implements UserDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(PlayerUserDetails.class)
    @Autowired
    PlayerRepository playerRepository

    @Override
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        List<Player> players = playerRepository.findBySourceAndSourceId(ManualPlayer.MANUAL_SOURCE, username)
        if (players.size() == 1) {
            return new PlayerUserDetails(players[0])
        } else if (players.size() == 0) {
            return null;
        } else {
            logger.warn("Found more than one manual player with username = " + username)
            return null
        }
    }
}
