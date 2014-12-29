package com.jtbdevelopment.gamecore.security.spring.security

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.gamecore.players.ManualPlayer
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerRoles
import org.bson.types.ObjectId
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Date: 12/24/14
 * Time: 3:06 PM
 */
class PlayerUserDetailsTest extends TwistedHangmanTestCase {
    void testGetSessionUser() {
        PlayerUserDetails d = new PlayerUserDetails(PONE)
        assert d.sessionUser.is(PONE)
    }

    void testGetEffectiveUser() {
        PlayerUserDetails d = new PlayerUserDetails(PONE)
        assert d.sessionUser.is(PONE)
        assert d.effectiveUser.is(PONE)
    }

    void testSetEffectiveUser() {
        PlayerUserDetails d = new PlayerUserDetails(PONE)
        assert d.sessionUser.is(PONE)
        assert d.effectiveUser.is(PONE)
        d.setEffectiveUser(PTWO)
        assert d.sessionUser.is(PONE)
        assert d.effectiveUser.is(PTWO)
    }

    void testGetUserId() {
        PlayerUserDetails d = new PlayerUserDetails(PONE)
        d.setEffectiveUser(PTWO)
        assert PONE.id.toHexString() == d.userId
    }

    void testGetAuthorities() {
        assert new PlayerUserDetails(new ManualPlayer()).authorities == [new SimpleGrantedAuthority(PlayerRoles.PLAYER)]
        assert new PlayerUserDetails(new ManualPlayer(adminUser: true)).authorities == [
                new SimpleGrantedAuthority(PlayerRoles.PLAYER),
                new SimpleGrantedAuthority(PlayerRoles.ADMIN),
        ]
    }

    void testGetPassword() {
        assert new PlayerUserDetails(new Player()).password == null
        assert new PlayerUserDetails(new ManualPlayer(password: "1")).password == "1"
    }

    void testGetUsername() {
        ObjectId pid = new ObjectId()
        assert new PlayerUserDetails(new Player(id: pid)).username == pid.toHexString()
        assert new PlayerUserDetails(new ManualPlayer(id: pid, sourceId: "ha")).username == "ha"
    }

    void testIsAccountNonExpired() {
        assert new PlayerUserDetails(new Player()).accountNonExpired
    }

    void testIsAccountNonLocked() {
        assert new PlayerUserDetails(new Player()).accountNonLocked
        assert new PlayerUserDetails(new ManualPlayer(verified: true)).accountNonLocked
        assert !(new PlayerUserDetails(new ManualPlayer(verified: false)).accountNonLocked)
    }

    void testIsCredentialsNonExpired() {
        assert new PlayerUserDetails(new Player()).credentialsNonExpired
    }

    void testIsEnabled() {
        assert new PlayerUserDetails(new Player()).enabled
        assert !(new PlayerUserDetails(new Player(disabled: true)).enabled)
    }
}
