package com.jtbdevelopment.TwistedHangman.security.spring.security

import com.jtbdevelopment.TwistedHangman.players.ManualPlayer
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.PlayerRoles
import com.jtbdevelopment.TwistedHangman.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.security.SocialUserDetails

/**
 * Date: 12/14/14
 * Time: 5:27 PM
 *
 * TODO - lots of cleanup
 *
 */
@CompileStatic
class PlayerUserDetails implements SocialUserDetails, UserDetails, SessionUserInfo {

    final Player player
    Player effectivePlayer

    final List<SimpleGrantedAuthority> grantedAuthorities = [new SimpleGrantedAuthority(PlayerRoles.PLAYER)]

    PlayerUserDetails(final Player player) {
        this.player = player
        this.effectivePlayer = player
        if (player.adminUser) {
            grantedAuthorities.push(new SimpleGrantedAuthority(PlayerRoles.ADMIN))
        }
    }

    @Override
    Player getSessionUser() {
        return player
    }

    @Override
    Player getEffectiveUser() {
        return effectivePlayer
    };

    @Override
    void setEffectiveUser(final Player player) {
        this.effectivePlayer = player
    }

    @Override
    String getUserId() {
        return player.id.toHexString()
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities
    }

    @Override
    String getPassword() {
        if (player instanceof ManualPlayer) {
            return player.password
        }
        return null
    }

    @Override
    String getUsername() {
        if (player instanceof ManualPlayer) {
            return player.getSourceId()
        }
        return player.id.toHexString()
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        if (player instanceof ManualPlayer) {
            return player.verified
        }
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return !player.disabled
    }
}
