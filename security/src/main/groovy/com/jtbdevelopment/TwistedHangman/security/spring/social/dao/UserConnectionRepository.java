package com.jtbdevelopment.TwistedHangman.security.spring.social.dao;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Date: 12/16/14
 * Time: 1:12 PM
 */
@Repository
public interface UserConnectionRepository extends PagingAndSortingRepository<UserConnection, ObjectId> {
    List<UserConnection> findByUserId(final String userId, final Sort sort);

    List<UserConnection> findByUserIdAndProviderId(final String userId, final String providerId);

    List<UserConnection> findByUserIdAndProviderId(final String userId, final String providerId, final Sort sort);

    List<UserConnection> findByUserIdAndProviderIdAndProviderUserId(final String userId, final String providerId, final String providerUserId);

    List<UserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(final String userId, final String providerId, final Collection<String> providerUserIds, final Sort sort);

    List<UserConnection> findByProviderIdAndProviderUserId(final String providerId, final String providerUserId);

    List<UserConnection> findByProviderIdAndProviderUserIdIn(final String providerId, final Collection<String> providerUserIds);

    Long deleteByUserIdAndProviderId(final String userId, final String providerId);

    Long deleteByUserIdAndProviderIdAndProviderUserId(final String userId, final String providerId, final String providerUserId);
}
