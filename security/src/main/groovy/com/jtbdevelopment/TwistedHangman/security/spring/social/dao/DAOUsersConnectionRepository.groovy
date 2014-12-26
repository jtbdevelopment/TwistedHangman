package com.jtbdevelopment.TwistedHangman.security.spring.social.dao

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.*
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 12/16/14
 * Time: 12:59 PM
 */
@CompileStatic
@Component
class DAOUsersConnectionRepository implements UsersConnectionRepository {
    @Autowired
    UserConnectionRepository userConnectionRepository

    @Autowired(required = false)
    ConnectionSignUp connectionSignUp

    @Autowired
    ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    TextEncryptor textEncryptor

    @PostConstruct
    public void setUp() {
        DAOConnectionRepository.userConnectionRepository = userConnectionRepository
        DAOConnectionRepository.connectionFactoryLocator = connectionFactoryLocator;
        DAOConnectionRepository.encryptor = textEncryptor
        DAOConnectionRepository.providerConnectionFactoryMap = connectionFactoryLocator.registeredProviderIds().collectEntries {
            String providerId ->
                [(providerId): connectionFactoryLocator.getConnectionFactory(providerId)]
        }
    }

    @Override
    List<String> findUserIdsWithConnection(final Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<UserConnection> connections = userConnectionRepository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        if (connections.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }
        return connections.collect { UserConnection it -> it.userId };
    }

    @Override
    Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
        List<UserConnection> connections = userConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds)
        return connections.collect { UserConnection it -> it.userId } as Set;
    }

    @Override
    ConnectionRepository createConnectionRepository(final String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new DAOConnectionRepository(userId);
    }
}
