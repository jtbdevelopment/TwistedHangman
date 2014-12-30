package com.jtbdevelopment.gamecore.security

import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic

/**
 * Date: 12/30/2014
 * Time: 12:06 PM
 */
@CompileStatic
interface SessionUserInfo<ID extends Serializable> {
    PlayerInt<ID> getSessionUser();

    PlayerInt<ID> getEffectiveUser();

    void setEffectiveUser(final PlayerInt<ID> player);
}