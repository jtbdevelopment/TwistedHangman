package com.jtbdevelopment.TwistedHangman.security.spring.social

import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository

/**
 * Date: 12/16/14
 * Time: 12:59 PM
 */
class MongoUserConnectionRepository implements UsersConnectionRepository {
    @Override
    List<String> findUserIdsWithConnection(final Connection<?> connection) {
        return null
    }

    @Override
    Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
        return null
    }

    @Override
    ConnectionRepository createConnectionRepository(final String userId) {
        return null
    }
}
