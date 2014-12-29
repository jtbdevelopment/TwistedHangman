package com.jtbdevelopment.gamecore.security.spring.security.dao;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 12/26/14
 * Time: 9:07 AM
 */
@Repository
public interface PersistentRememberMeTokenRepository extends CrudRepository<RememberMeToken, ObjectId> {
    RememberMeToken findBySeries(final String series);

    List<RememberMeToken> findByUsername(final String username);
}
