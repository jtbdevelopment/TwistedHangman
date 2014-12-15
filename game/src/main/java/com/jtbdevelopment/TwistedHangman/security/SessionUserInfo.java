package com.jtbdevelopment.TwistedHangman.security;

import com.jtbdevelopment.TwistedHangman.players.Player;

/**
 * Date: 12/15/14
 * Time: 7:05 AM
 */
public interface SessionUserInfo {
    public Player getSessionUser();

    public Player getEffectiveUser();

    public void setEffectiveUser(final Player player);
}
