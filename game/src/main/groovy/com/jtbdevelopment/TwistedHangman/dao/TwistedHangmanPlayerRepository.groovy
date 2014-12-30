package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.gamecore.mongo.dao.MongoPlayerRepository
import groovy.transform.CompileStatic
import org.springframework.stereotype.Repository

/**
 * Date: 12/30/2014
 * Time: 11:12 AM
 */
@Repository
@CompileStatic
interface TwistedHangmanPlayerRepository extends MongoPlayerRepository {
}
