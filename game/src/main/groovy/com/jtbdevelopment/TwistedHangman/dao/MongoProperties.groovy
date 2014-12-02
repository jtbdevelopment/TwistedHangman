package com.jtbdevelopment.TwistedHangman.dao

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Date: 12/1/14
 * Time: 10:24 PM
 */
@Component
@CompileStatic
class MongoProperties {
    @Value('${mongo.dbName:twistedhangman}')
    String dbName;
    @Value('${mongo.host:localhost}')
    String dbHost;
    @Value('${mongo.userName:twistedhangman}')
    String dbUser;
    @Value('${mongo.userPassword:twistedhangman}')
    String dbPassword;
}
