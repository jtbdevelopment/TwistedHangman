package com.jtbdevelopment.gamecore.security;

import com.jtbdevelopment.gamecore.players.PlayerInt;

import java.io.Serializable;

/**
 * Date: 12/15/14
 * Time: 7:05 AM
 */
public interface SessionUserInfo<ID extends Serializable> {
    public PlayerInt<ID> getSessionUser();

    public PlayerInt<ID> getEffectiveUser();

    public void setEffectiveUser(final PlayerInt<ID> player);
}
