package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.gamecore.dao.mongo.MongoConfiguration
import com.jtbdevelopment.gamecore.dao.mongo.MongoProperties
import com.jtbdevelopment.gamecore.dao.mongo.converters.MongoConverter
import org.springframework.core.convert.support.GenericConversionService
import org.springframework.data.authentication.UserCredentials

/**
 * Date: 12/22/14
 * Time: 11:52 AM
 */
class MongoConfigurationTest extends GroovyTestCase {
    private MongoConfiguration mongoConfiguration = new MongoConfiguration()

    void testGetDatabaseName() {
        MongoProperties properties = new MongoProperties(dbName: "DBISX")
        mongoConfiguration.mongoProperties = properties
        assert mongoConfiguration.databaseName == properties.dbName
    }

    void testGetMappingBasePackage() {
        assert mongoConfiguration.mappingBasePackage == "com.jtbdevelopment"
    }

    void testGetUserCredentials() {
        MongoProperties properties = new MongoProperties(dbUser: "SOMEUSER", dbPassword: "APASS")
        mongoConfiguration.mongoProperties = properties
        UserCredentials credentials = mongoConfiguration.userCredentials
        assert credentials.password == properties.dbPassword
        assert credentials.username == properties.dbUser
    }

    private static class ConvertibleClass {

    }

    void testCustomConversions() {
        def cc1 = new MongoConverter<String, ConvertibleClass>() {
            @Override
            ConvertibleClass convert(final String source) {
                return null
            }
        }
        def cc2 = new MongoConverter<ConvertibleClass, Integer>() {
            @Override
            Integer convert(final ConvertibleClass source) {
                return null
            }
        }

        mongoConfiguration.mongoConverters = [cc1, cc2]
        def service = new GenericConversionService()
        mongoConfiguration.customConversions().registerConvertersIn(service)
        assert service.canConvert(String.class, ConvertibleClass.class)
        assert service.canConvert(ConvertibleClass.class, Integer.class)
    }

    //  Mongo auto connects, tough to unit test
    /*
    void testMongo() {
    }
    */
}
