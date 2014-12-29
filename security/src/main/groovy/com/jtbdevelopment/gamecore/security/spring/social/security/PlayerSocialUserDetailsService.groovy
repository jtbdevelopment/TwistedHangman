package com.jtbdevelopment.gamecore.security.spring.social.security

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.security.spring.security.PlayerUserDetails
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.social.security.SocialUserDetails
import org.springframework.social.security.SocialUserDetailsService
import org.springframework.stereotype.Component

/**
 * Date: 12/13/14
 * Time: 9:36 PM
 *
 */
@Component
@CompileStatic
class PlayerSocialUserDetailsService implements SocialUserDetailsService {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    SocialUserDetails loadUserByUserId(final String userId) throws UsernameNotFoundException, DataAccessException {
        Player p = playerRepository.findOne(new ObjectId(userId));
        return (p != null ? new PlayerUserDetails(p) : null);
    }
}
