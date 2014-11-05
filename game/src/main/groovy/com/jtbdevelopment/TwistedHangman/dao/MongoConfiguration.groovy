package com.jtbdevelopment.TwistedHangman.dao

import com.mongodb.Mongo
import com.mongodb.WriteConcern
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Date: 2/23/14
 * Time: 6:17 PM
 */
@Configuration
@EnableMongoRepositories("com.jtbdevelopment")
class MongoConfiguration extends AbstractMongoConfiguration {
    String test = "X"
    @Value('${mongo.dbName:twistedhangman}')
    String dbName;
    @Value('${mongo.host:localhost}')
    String dbHost;
    @Value('${mongo.userName:twistedhangman}')
    String dbUser;
    @Value('${mongo.userPassword:twistedhangman}')
    String dbPassword;

    @Override
    protected String getDatabaseName() {
        return dbName
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.jtbdevelopment"
    }

    @Override
    protected UserCredentials getUserCredentials() {
        return new UserCredentials(dbUser, dbPassword)
    }

    @Override
    Mongo mongo() throws Exception {
        Mongo mongo = new Mongo(dbHost)
        mongo.setWriteConcern(WriteConcern.JOURNALED)
        return mongo
    }
}
