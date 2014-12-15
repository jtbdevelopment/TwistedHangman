package com.jtbdevelopment.TwistedHangman.security

import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.PlayerRoles
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.social.security.SocialUserDetails

/**
 * Date: 12/14/14
 * Time: 5:27 PM
 *
 * TODO - lots of cleanup
 *
 */
@CompileStatic
class PlayerUserDetails implements SocialUserDetails, SessionUserInfo {

    final Player player
    Player effectivePlayer

    final List<SimpleGrantedAuthority> grantedAuthorities = [new SimpleGrantedAuthority(PlayerRoles.PLAYER)]

    PlayerUserDetails(final Player player) {
        this.player = player
        this.effectivePlayer = player
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
        return player.id
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities
    }

    @Override
    String getPassword() {
        return null
    }

    @Override
    String getUsername() {
        return player.id
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
