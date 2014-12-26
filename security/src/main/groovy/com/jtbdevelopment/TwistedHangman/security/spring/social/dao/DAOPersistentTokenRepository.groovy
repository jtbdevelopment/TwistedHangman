package com.jtbdevelopment.TwistedHangman.security.spring.social.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.stereotype.Component

/**
 * Date: 12/26/14
 * Time: 9:08 AM
 */
@Component
class DAOPersistentTokenRepository implements PersistentTokenRepository {
    @Autowired
    PersistentRememberMeTokenRepository persistentRememberMeTokenRepository

    @Override
    void createNewToken(final PersistentRememberMeToken token) {
        persistentRememberMeTokenRepository.save(new MongoPersistentRememberMeToken(token))
    }

    @Override
    void updateToken(final String series, final String tokenValue, final Date lastUsed) {
        MongoPersistentRememberMeToken mongoPersistentRememberMeToken = persistentRememberMeTokenRepository.findBySeries(series)
        if (mongoPersistentRememberMeToken) {
            mongoPersistentRememberMeToken.tokenValue = tokenValue
            mongoPersistentRememberMeToken.date = lastUsed
            persistentRememberMeTokenRepository.save(mongoPersistentRememberMeToken)
        }
    }

    @Override
    PersistentRememberMeToken getTokenForSeries(final String seriesId) {
        return persistentRememberMeTokenRepository.findBySeries(seriesId)
    }

    @Override
    void removeUserTokens(final String username) {
        persistentRememberMeTokenRepository.delete(persistentRememberMeTokenRepository.findByUsername(username))
    }
}
