package com.jtbdevelopment.TwistedHangman.security.spring.social.dao

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken

/**
 * Date: 12/26/14
 * Time: 9:03 AM
 */
@Document(collection = 'persistentTokens')
@CompoundIndexes(
        [@CompoundIndex(name = 'series', unique = true, def = "{'series':1}")]
)
class MongoPersistentRememberMeToken extends PersistentRememberMeToken {
    @Id
    ObjectId id

    MongoPersistentRememberMeToken(
            final String username, final String series, final String tokenValue, final Date date) {
        super(username, series, tokenValue, date)
    }

    MongoPersistentRememberMeToken(PersistentRememberMeToken persistentRememberMeToken) {
        this(persistentRememberMeToken.username,
                persistentRememberMeToken.series,
                persistentRememberMeToken.tokenValue,
                persistentRememberMeToken.date)
    }
}
