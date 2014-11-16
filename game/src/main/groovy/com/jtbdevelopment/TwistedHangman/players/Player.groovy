package com.jtbdevelopment.TwistedHangman.players

import groovy.transform.CompileStatic
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.util.StringUtils

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@Document
//  This propagating to game table...
//@CompoundIndex(unique = true, name = "id_source", def = "{'_id':1, 'source':1}")
@CompileStatic
class Player {

    public static final String SYSTEM_ID_DISPLAY_NAME = "TwistedHangman"
    public static final String SYSTEM_ID_SOURCE = "System"
    public static final String SYSTEM_ID_ID = "0"
    public static final String SYSYEM_ID_MD5 = "c2a4661ec539e34fee2e6f94f2befdf6"
    public static final Player SYSTEM_PLAYER = new Player(
            id: SYSTEM_ID_ID,
            displayName: SYSTEM_ID_DISPLAY_NAME,
            source: SYSTEM_ID_SOURCE)

    @Id
    String id
    String source
    String displayName
    @Indexed
    private String md5
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

    void setId(final String id) {
        this.id = id
        computeMD5Hex()
    }

    void setSource(final String source) {
        this.source = source
        computeMD5Hex()
    }

    void setDisplayName(final String displayName) {
        this.displayName = displayName
        computeMD5Hex()
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

    String getMd5() {
        if (StringUtils.isEmpty(md5)) {
            computeMD5Hex()
        }
        return md5
    }

    private String computeMD5Hex() {
        String key = id + source + displayName
        md5 = DigestUtils.md5Hex(key)
        md5
    }
}
