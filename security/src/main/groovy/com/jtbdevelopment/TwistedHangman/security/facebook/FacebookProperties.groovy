package com.jtbdevelopment.TwistedHangman.security.facebook

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.annotation.PostConstruct

/**
 * Date: 12/13/14
 * Time: 9:03 PM
 */
@CompileStatic
@Component
class FacebookProperties {
    private static final Logger logger = LoggerFactory.getLogger(FacebookProperties.class)
    @Value('${facebook.clientID:#{null}}')
    String clientID;
    @Value('${facebook.clientSecret:#{null}}')
    String clientSecret;

    @PostConstruct
    public void testDefaults() {
        if (StringUtils.isEmpty(clientID) || StringUtils.isEmpty(clientSecret)) {
            logger.warn('----------------------------------------------------------------------------------------------')
            logger.warn('----------------------------------------------------------------------------------------------')
            logger.warn('----------------------------------------------------------------------------------------------')
            logger.warn('facebook.clientID AND/OR facebook.clientSecret is using default values.  Not likely to work!!!')
            logger.warn('----------------------------------------------------------------------------------------------')
            logger.warn('----------------------------------------------------------------------------------------------')
            logger.warn('----------------------------------------------------------------------------------------------')
        }
    }
}
