package com.jtbdevelopment.TwistedHangman.players

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.jtbdevelopment.TwistedHangman.json.ObjectIdDeserializer
import com.jtbdevelopment.TwistedHangman.json.ObjectIdSerializer
import groovy.transform.CompileStatic
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
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
//@CompoundIndex(unique = true, name = "id_source", def = "{'sourceId':1, 'source':1}")
@CompileStatic

class Player implements Cloneable {
    //  Klunky - but initialized by FriendMasker
    public static String ID_SALT = ""

    public static final String SYSTEM_ID_DISPLAY_NAME = "TwistedHangman"
    public static final String SYSTEM_ID_SOURCE = "System"
    public static final ObjectId SYSTEM_ID_ID = new ObjectId("".padRight(24, "0"));
    public static final Player SYSTEM_PLAYER = new Player(
            id: SYSTEM_ID_ID,
            displayName: SYSTEM_ID_DISPLAY_NAME,
            source: SYSTEM_ID_SOURCE,
            sourceId: SYSTEM_ID_ID.toHexString())
    public static final String SYSTEM_ID_MD5 = SYSTEM_PLAYER.md5

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    ObjectId id = new ObjectId()
    String source
    String sourceId
    String displayName
    String imageUrl
    String profileUrl

    @Indexed
    private String md5
    boolean disabled = false
    boolean adminUser = false

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

    void setId(final ObjectId id) {
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

    void setSourceId(final String sourceId) {
        this.sourceId = sourceId
        computeMD5Hex()
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", sourceId='" + sourceId + '\'' +
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
        if (id == null || source == null || displayName == null || sourceId == null) {
            md5 = ""
        } else {
            String key = ID_SALT + id.toHexString() + source + ID_SALT + displayName + sourceId + ID_SALT
            md5 = DigestUtils.md5Hex(key)
        }
        md5
    }
}
