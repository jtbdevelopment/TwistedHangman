package com.jtbdevelopment.TwistedHangman.security.spring.social.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Date: 12/16/14
 * Time: 1:12 PM
 */
@Repository
public interface JTBUserConnectionRepository extends CrudRepository<JTBUserConnection, ObjectId> {
}
