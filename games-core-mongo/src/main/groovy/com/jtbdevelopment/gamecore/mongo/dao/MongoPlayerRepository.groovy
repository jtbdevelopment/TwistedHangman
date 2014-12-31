package com.jtbdevelopment.gamecore.mongo.dao

import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

/**
 * Date: 12/30/2014
 * Time: 11:06 AM
 */
@CompileStatic
@Repository
interface MongoPlayerRepository extends AbstractPlayerRepository<ObjectId> {

}