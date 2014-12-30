package com.jtbdevelopment.gamecore.security.spring.security

import com.jtbdevelopment.gamecore.players.ManualPlayer
import com.jtbdevelopment.gamecore.players.PlayerInt
import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.security.SocialUserDetails

/**
 * Date: 12/14/14
 * Time: 5:27 PM
 */
@CompileStatic
class PlayerUserDetails<ID extends Serializable> implements SocialUserDetails, UserDetails, SessionUserInfo {

    final PlayerInt<ID> player
    PlayerInt<ID> effectivePlayer

    final List<SimpleGrantedAuthority> grantedAuthorities = [new SimpleGrantedAuthority(PlayerRoles.PLAYER)]

    PlayerUserDetails(final PlayerInt<ID> player) {
        this.player = player
        this.effectivePlayer = player
        if (player.adminUser) {
            grantedAuthorities.push(new SimpleGrantedAuthority(PlayerRoles.ADMIN))
        }
    }

    @Override
    PlayerInt<ID> getSessionUser() {
        return player
    }

    @Override
    PlayerInt<ID> getEffectiveUser() {
        return effectivePlayer
    }

    @Override
    void setEffectiveUser(final PlayerInt player) {
        this.effectivePlayer = player
    }

    @Override
    String getUserId() {
        return player.idAsString
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
        return player.idAsString
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
