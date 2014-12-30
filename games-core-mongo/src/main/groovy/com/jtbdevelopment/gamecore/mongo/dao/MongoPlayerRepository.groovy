package com.jtbdevelopment.gamecore.mongo.dao

import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.repository.NoRepositoryBean

/**
 * Date: 12/30/2014
 * Time: 11:06 AM
 */
@CompileStatic
@NoRepositoryBean
interface MongoPlayerRepository extends AbstractPlayerRepository<ObjectId> {

}