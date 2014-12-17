package com.jtbdevelopment.TwistedHangman.security.spring.social.dao

import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 12/16/14
 * Time: 1:04 PM
 */
@Document(collection = 'userConnection')
@CompoundIndexes(
        [
                @CompoundIndex(name = "uc_created", unique = true, def = "{'userId': 1, 'providerId': 1, 'created': 1}"),
                @CompoundIndex(name = "uc_pk", unique = true, def = "{'userId': 1, 'providerId': 1, 'providerUserId': 1}"),
        ]
)
@CompileStatic
class UserConnection {
    private static final ZoneId GMT = ZoneId.of("GMT")
    @Id
    ObjectId id  // generated internal
    @Version
    Integer version // for optmistic locking
    ZonedDateTime created = ZonedDateTime.now(GMT)

    String userId

    String providerId
    String providerUserId

    String displayName
    String profileUrl
    String imageUrl

    //  these 3 should be encrypted?
    String accessToken
    String secret
    String refreshToken

    Long expireTime
}
