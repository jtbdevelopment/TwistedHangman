package com.jtbdevelopment.gamecore.security

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Date: 12/20/2014
 * Time: 4:48 PM
 */
@Component
@CompileStatic
class SecurityProperties {
    @Value('${http.allowBasicAuth:false}')
    String allowBasicAuth
}
