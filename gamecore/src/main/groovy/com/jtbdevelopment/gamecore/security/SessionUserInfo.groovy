package com.jtbdevelopment.gamecore.security

import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic

/**
 * Date: 12/30/2014
 * Time: 12:06 PM
 */
@CompileStatic
interface SessionUserInfo<ID extends Serializable> {
    Player<ID> getSessionUser();

    Player<ID> getEffectiveUser();

    void setEffectiveUser(final Player<ID> player);
}