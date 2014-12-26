package com.jtbdevelopment.TwistedHangman.security.spring.social.dao;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 12/26/14
 * Time: 9:07 AM
 */
@Repository
public interface PersistentRememberMeTokenRepository extends CrudRepository<MongoPersistentRememberMeToken, ObjectId> {
    MongoPersistentRememberMeToken findBySeries(final String series);

    List<MongoPersistentRememberMeToken> findByUsername(final String username);
}
