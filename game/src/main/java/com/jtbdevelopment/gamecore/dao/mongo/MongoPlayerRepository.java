package com.jtbdevelopment.gamecore.dao.mongo;

import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 * <p>
 * TODO - cache
 */
@NoRepositoryBean
public interface MongoPlayerRepository extends AbstractPlayerRepository<ObjectId> {
}
