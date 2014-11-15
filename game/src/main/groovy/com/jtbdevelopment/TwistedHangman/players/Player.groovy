package com.jtbdevelopment.TwistedHangman.players

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@Document
class Player {

    public static final String SYSTEM_ID_DISPLAY_NAME = "TwistedHangman"
    public static final String SYSTEM_ID_SOURCE = "System"
    public static final String SYSTEM_ID_ID = "0"
    public static final Player SYSTEM_PLAYER = new Player(
            id: SYSTEM_ID_ID,
            displayName: SYSTEM_ID_DISPLAY_NAME,
            source: SYSTEM_ID_SOURCE)

    @Id
    String id
    String source
    String displayName
    boolean disabled = false

    boolean equals(final o) {
        if (this.is(o)) return true
        if (!(o instanceof Player)) return false

        final Player player = (Player) o

        if (id != player.id) return false

        return true
    }

    int hashCode() {
        return id.hashCode()
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", displayName='" + displayName + '\'' +
                ", disabled=" + disabled +
                '}';
    }

    @Transient
    private String md5Hex

    String getMd5Hex() {
        if (md5Hex == null) {
            md5Hex = DigestUtils.md5Hex((id + source + displayName))
        }
        return md5Hex
    }
}
