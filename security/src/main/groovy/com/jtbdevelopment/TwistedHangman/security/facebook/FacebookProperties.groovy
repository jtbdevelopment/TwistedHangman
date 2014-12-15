package com.jtbdevelopment.TwistedHangman.security.facebook

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Date: 12/13/14
 * Time: 9:03 PM
 */
@CompileStatic
@Component
class FacebookProperties {
    @Value('${facebook.clientID:NOTSET}')
    String clientID;
    @Value('${facebook.clientSecret:NOTSET}')
    String clientSecret;
}
