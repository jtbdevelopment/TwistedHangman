package com.jtbdevelopment.TwistedHangman.security.spring.social.mongo

import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 12/16/14
 * Time: 1:04 PM
 */
@Document(collection = 'userConnection')
@CompoundIndexes(
        [
                @CompoundIndex(name = "uc_pk", unique = true, def = "{'userId': 1, 'providerId': 1, 'providerUserId': 1}"),
                @CompoundIndex(name = "uc_pk", unique = true, def = "{'userId': 1, 'providerId': 1, 'rank': 1}"),
        ]
)
@CompileStatic
class JTBUserConnection {

    @Id
    ObjectId id  // generated internal

    String userId
    String providerId
    String providerUserId

    int rank
    String displayName
    String profileUrl
    String imageUrl
    String accessToken
    String secret
    String refreshToken
    long expireTime
}
