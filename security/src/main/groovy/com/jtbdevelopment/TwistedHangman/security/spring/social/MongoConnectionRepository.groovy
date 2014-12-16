package com.jtbdevelopment.TwistedHangman.security.spring.social

import com.jtbdevelopment.TwistedHangman.security.spring.social.mongo.JTBUserConnectionRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionKey
import org.springframework.social.connect.ConnectionRepository
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

/**
 * Date: 12/16/14
 * Time: 1:17 PM
 */
@Component
@CompileStatic
class MongoConnectionRepository implements ConnectionRepository {
    @Autowired
    JTBUserConnectionRepository jtbUserConnectionRepository

    @Override
    MultiValueMap<String, Connection<?>> findAllConnections() {
        return null
    }

    @Override
    List<Connection<?>> findConnections(final String providerId) {
        return null
    }

    @Override
    def <A> List<Connection<A>> findConnections(final Class<A> apiType) {
        return null
    }

    @Override
    MultiValueMap<String, Connection<?>> findConnectionsToUsers(final MultiValueMap<String, String> providerUserIds) {
        return null
    }

    @Override
    Connection<?> getConnection(final ConnectionKey connectionKey) {
        return null
    }

    @Override
    def <A> Connection<A> getConnection(final Class<A> apiType, final String providerUserId) {
        return null
    }

    @Override
    def <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
        return null
    }

    @Override
    def <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
        return null
    }

    @Override
    void addConnection(final Connection<?> connection) {

    }

    @Override
    void updateConnection(final Connection<?> connection) {

    }

    @Override
    void removeConnections(final String providerId) {

    }

    @Override
    void removeConnection(final ConnectionKey connectionKey) {

    }
}
