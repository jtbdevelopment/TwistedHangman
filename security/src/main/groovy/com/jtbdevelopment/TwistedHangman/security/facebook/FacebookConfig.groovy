package com.jtbdevelopment.TwistedHangman.security.facebook

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.facebook.security.FacebookAuthenticationService

/**
 * Date: 12/16/14
 * Time: 12:56 PM
 */
@Configuration
@CompileStatic
class FacebookConfig {
    @Bean
    @Autowired
    FacebookAuthenticationService facebookAuthenticationService(final FacebookProperties facebookProperties) {
        return new FacebookAuthenticationService(facebookProperties.getClientID(), facebookProperties.getClientSecret());
    }
}
